package com.meliksahcakir.spacedelivery.shuttle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meliksahcakir.spacedelivery.R
import com.meliksahcakir.spacedelivery.data.Shuttle
import com.meliksahcakir.spacedelivery.utils.Event
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class ShuttleViewModel : ViewModel() {

    companion object {
        const val MAX_AVAILABLE_POINT = 15
    }

    private val _durability = MutableLiveData(1)
    val durability: LiveData<Int> = _durability
    private val _velocity = MutableLiveData(1)
    val velocity: LiveData<Int> = _velocity
    private val _capacity = MutableLiveData(1)
    val capacity: LiveData<Int> = _capacity
    var name = ""

    private val _availablePoints = MutableLiveData(12)
    val availablePoints: LiveData<Int> = _availablePoints

    private val _navigateToMainScreenEvent = MutableLiveData<Event<Shuttle>>()
    val navigateToMainScreenEvent: LiveData<Event<Shuttle>> = _navigateToMainScreenEvent

    private val _snackBarMessage = MutableLiveData<Event<Int>>()
    val snackBarMessage: LiveData<Event<Int>> = _snackBarMessage

    fun onNameChanged(name: String) {
        this.name = name
    }

    fun onDurabilityChanged(value: Int) {
        val durability = max(value, 1)
        _durability.value = min(durability, getMaxDurabilityAllowed())
        _availablePoints.value = calculateAvailablePoints()
    }

    fun onVelocityChanged(value: Int) {
        val velocity = max(value, 1)
        _velocity.value = min(velocity, getMaxVelocityAllowed())
        _availablePoints.value = calculateAvailablePoints()
    }

    fun onCapacityChanged(value: Int) {
        val capacity = max(value, 1)
        _capacity.value = min(capacity, getMaxCapacityAllowed())
        _availablePoints.value = calculateAvailablePoints()
    }

    private fun calculateAvailablePoints(): Int {
        return MAX_AVAILABLE_POINT - getDurability() - getVelocity() - getCapacity()
    }

    fun getMaxDurabilityAllowed(): Int {
        return MAX_AVAILABLE_POINT - getVelocity() - getCapacity()
    }

    fun getMaxVelocityAllowed(): Int {
        return MAX_AVAILABLE_POINT - getDurability() - getCapacity()
    }

    fun getMaxCapacityAllowed(): Int {
        return MAX_AVAILABLE_POINT - getDurability() - getVelocity()
    }

    fun onStartButtonClicked() {
        if (name == "") {
            _snackBarMessage.value = Event(R.string.shuttle_name_cannot_be_empty)
        } else {
            val shuttle = Shuttle(name, getCapacity(), getVelocity(), getDurability())
            _navigateToMainScreenEvent.value = Event(shuttle)
        }
    }

    private fun getDurability() = _durability.value ?: 1
    private fun getVelocity() = _velocity.value ?: 1
    private fun getCapacity() = _capacity.value ?: 1
}