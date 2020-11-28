package com.meliksahcakir.spacedelivery.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.meliksahcakir.spacedelivery.data.repository.IStationsRepository
import com.meliksahcakir.spacedelivery.splash.SplashViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: IStationsRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(SplashViewModel::class.java) ->
                    SplashViewModel(repository)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
    }
}