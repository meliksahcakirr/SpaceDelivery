package com.meliksahcakir.spacedelivery.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.meliksahcakir.spacedelivery.data.repository.ISpaceDeliveryRepository
import com.meliksahcakir.spacedelivery.utils.Event
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: ISpaceDeliveryRepository) : ViewModel() {

    private var initialized = false

    private val _navigateToNextScreen = MutableLiveData<Event<Unit>>()
    val navigateToNextScreen: LiveData<Event<Unit>> = _navigateToNextScreen

    init {
        start()
        viewModelScope.launch {
            delay(2000)
            _navigateToNextScreen.postValue(Event(Unit))
        }
    }

    private fun start() {
        if (!initialized) {
            viewModelScope.launch {
                repository.refreshStations()
            }
            initialized = true
        }
    }
}
