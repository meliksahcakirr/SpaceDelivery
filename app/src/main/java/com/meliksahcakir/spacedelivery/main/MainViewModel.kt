package com.meliksahcakir.spacedelivery.main

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.meliksahcakir.spacedelivery.data.Shuttle
import com.meliksahcakir.spacedelivery.utils.Event

class MainViewModel : ViewModel() {

    private var shuttle: Shuttle? = null

    private val _ugs = MutableLiveData(10000)
    val ugs: LiveData<Int> = _ugs

    private val _eus = MutableLiveData(1000)
    val eus: LiveData<Int> = _eus

    private val _ds = MutableLiveData(10000)
    val ds: LiveData<Int> = _ds

    private val _health = MutableLiveData(100)
    val health: LiveData<Int> = _health

    private val _timer = MutableLiveData(10)
    val timer: LiveData<Int> = _timer

    private val _gameOver = MutableLiveData<Event<Unit>>()
    val gameOver: LiveData<Event<Unit>> = _gameOver

    private val _shuttleName = MutableLiveData("")
    val shuttleName: LiveData<String> = _shuttleName

    private val handler = Handler()

    private var tickRunnable = object : Runnable {
        override fun run() {
            _timer.value?.let {
                if (it == 0) {
                    _health.value = _health.value?.minus(10)
                    _timer.value = shuttle?.durability?.times(10)
                    if (_health.value == 0) {
                        _gameOver.value = Event(Unit)
                        handler.removeCallbacks(this)
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
        _ugs.value = shuttle.capacity * 10000
        _eus.value = shuttle.velocity * 10000
        _ds.value = shuttle.durability * 10000
        _timer.value = shuttle.durability * 10
        _health.value = 100
        handler.postDelayed(tickRunnable, 1000)
        _shuttleName.value = shuttle.name
    }

    override fun onCleared() {
        super.onCleared()
        resetTimer()
    }

    fun resetTimer() {
        handler.removeCallbacks(tickRunnable)
    }
}