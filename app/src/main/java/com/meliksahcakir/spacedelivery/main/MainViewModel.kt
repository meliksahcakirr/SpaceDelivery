package com.meliksahcakir.spacedelivery.main

import android.os.Handler
import androidx.lifecycle.*
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.data.Shuttle
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.data.Statistics
import com.meliksahcakir.spacedelivery.data.repository.ISpaceDeliveryRepository
import com.meliksahcakir.spacedelivery.utils.Event
import com.meliksahcakir.spacedelivery.utils.Result
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(val repository: ISpaceDeliveryRepository) : ViewModel() {

    companion object {
        private const val UGS_MULTIPLIER = 10000
        private const val EUS_MULTIPLIER = 20
        private const val DS_MULTIPLIER = 10000
        private const val TIMER_MULTIPLIER = 10
        private const val MAX_HEALTH = 100
        private const val HEALTH_DECREMENT = 10
    }

    private var shuttle: Shuttle? = null

    private val _ugs = MutableLiveData(UGS_MULTIPLIER)
    val ugs: LiveData<Int> = _ugs

    private val _eus = MutableLiveData(EUS_MULTIPLIER)
    val eus: LiveData<Int> = _eus

    private val _ds = MutableLiveData(DS_MULTIPLIER)
    val ds: LiveData<Int> = _ds

    private val _health = MutableLiveData(MAX_HEALTH)
    val health: LiveData<Int> = _health

    private val _timer = MutableLiveData(TIMER_MULTIPLIER)
    val timer: LiveData<Int> = _timer

    private val _gameOver = MutableLiveData<Statistics?>()
    val gameOver: LiveData<Statistics?> = _gameOver

    private val _shuttleName = MutableLiveData("")
    val shuttleName: LiveData<String> = _shuttleName

    private val handler = Handler()

    private val _currentStation = MutableLiveData<Station>(Station.EARTH)
    val currentStation: LiveData<Station> = _currentStation

    private val _snackBarMessage = MutableLiveData<Event<Int>>()
    val snackBarMessage: LiveData<Event<Int>> = _snackBarMessage

    val stations: LiveData<List<Station>> = repository.observeStations().map {
        if (it is Result.Success) {
            it.data
        } else {
            emptyList()
        }
    }

    val favoriteStations = repository.observeFavoriteStations().map {
        if (it is Result.Success) {
            it.data
        } else {
            emptyList()
        }
    }

    private var _foundStation = MutableLiveData<Event<Station>>()
    val foundStation: LiveData<Event<Station>> = _foundStation

    private var tickRunnable = object : Runnable {
        override fun run() {
            _timer.value?.let {
                if (it == 0) {
                    _health.value = _health.value?.minus(HEALTH_DECREMENT)
                    _timer.value = shuttle?.durability?.times(TIMER_MULTIPLIER)
                    if (_health.value == 0) {
                        gameOver(R.string.your_spaceship_has_taken_too_much_damage)
                        return@let
                    }
                }
                _timer.value = _timer.value?.minus(1)
                handler.postDelayed(this, 1000)
            }
        }
    }

    fun onShuttleSelected(shuttle: Shuttle) {
        this.shuttle = shuttle
        _ugs.value = shuttle.capacity * UGS_MULTIPLIER
        _eus.value = shuttle.velocity * EUS_MULTIPLIER
        _ds.value = shuttle.durability * DS_MULTIPLIER
        _timer.value = shuttle.durability * TIMER_MULTIPLIER
        _health.value = MAX_HEALTH
        handler.postDelayed(tickRunnable, 1000)
        _shuttleName.value = shuttle.name
        viewModelScope.launch {
            repository.resetStations()
            startDeliveryInEarth()
        }
    }

    override fun onCleared() {
        super.onCleared()
        resetTimer()
    }

    fun resetTimer() {
        handler.removeCallbacks(tickRunnable)
    }

    fun onSearch(search: String) {
        if (stations.value != null) {
            val station = stations.value!!.find {
                it.name.toLowerCase(Locale.getDefault())
                    .startsWith(search.trim().toLowerCase(Locale.getDefault()))
            }
            if (station != null) {
                _foundStation.value = Event(station)
            } else {
                _snackBarMessage.value = Event(R.string.station_cannot_be_found)
            }
        } else {
            _snackBarMessage.value = Event(R.string.there_is_no_planet_to_travel)
        }
    }

    fun onTravelClicked(station: Station) {
        if (station.calculateEus(_currentStation.value!!) > _eus.value!!) {
            _snackBarMessage.value = Event(R.string.galaxy_far_far_away)
        } else {
            _eus.value = _eus.value?.minus(station.calculateEus(_currentStation.value!!))
            _ugs.value = station.deliverPackets(_ugs.value!!)
            _currentStation.value = station
            station.completed = true
            viewModelScope.launch {
                repository.updateStation(station)
                when {
                    _eus.value == 0 -> gameOver(R.string.your_time_eus_is_up)
                    _ugs.value == 0 -> gameOver(R.string.you_are_out_of_ugs)
                    else -> checkIfTheGameIsOver()
                }
            }
        }
    }

    private fun startDeliveryInEarth() {
        viewModelScope.launch {
            val result = repository.getStation(Station.EARTH.name)
            if (result is Result.Success) {
                val station = result.data
                _currentStation.value = station
                station.completed = true
                repository.updateStation(station)
            }
        }
    }

    fun onFavoriteClicked(station: Station, favorite: Boolean) {
        viewModelScope.launch {
            repository.updateStationFavStatus(station.name, favorite)
        }
    }

    private fun checkIfTheGameIsOver() {
        stations.value?.let {
            val eus = _eus.value ?: 0
            val active = it.filter { station -> !station.completed }
            if (active.isEmpty()) {
                gameOver(R.string.you_traveled_all_the_stations)
            } else {
                val st = active.find { st -> st.calculateEus(currentStation.value!!) <= eus }
                if (st == null) {
                    gameOver(R.string.all_the_stations_are_far_away)
                }
            }
        }
    }

    private fun gameOver(messageId: Int) {
        val deliveredUgs = shuttle?.capacity?.times(UGS_MULTIPLIER)?.minus(_ugs.value ?: 0) ?: 0
        val spentEus = shuttle?.velocity?.times(EUS_MULTIPLIER)?.minus(_eus.value ?: 0) ?: 0
        val numberOfDestination = stations.value?.filter { it.completed }?.size?.minus(1) ?: 0
        val name = _shuttleName.value ?: ""
        val statistics = Statistics(
            spentEus = spentEus,
            name = name,
            deliveredUgs = deliveredUgs,
            gameOverReason = messageId,
            numberOfDestination = numberOfDestination
        )
        _gameOver.value = statistics
        resetTimer()
        viewModelScope.launch {
            repository.addStatistics(statistics)
        }
    }

    fun gameOverHandled() {
        _gameOver.value = null
    }
}