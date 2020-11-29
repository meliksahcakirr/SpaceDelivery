package com.meliksahcakir.spacedelivery.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meliksahcakir.spacedelivery.statistics.StatisticsViewModel
import com.meliksahcakir.spacedelivery.data.repository.ISpaceDeliveryRepository
import com.meliksahcakir.spacedelivery.main.MainViewModel
import com.meliksahcakir.spacedelivery.splash.SplashViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: ISpaceDeliveryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(SplashViewModel::class.java) ->
                    SplashViewModel(repository)
                isAssignableFrom(MainViewModel::class.java) ->
                    MainViewModel(repository)
                isAssignableFrom(StatisticsViewModel::class.java) ->
                    StatisticsViewModel(
                        repository
                    )
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
    }
}