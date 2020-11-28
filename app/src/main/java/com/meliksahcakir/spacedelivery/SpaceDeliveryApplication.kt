package com.meliksahcakir.spacedelivery

import android.app.Application
import com.meliksahcakir.spacedelivery.data.repository.IStationsRepository
import com.meliksahcakir.spacedelivery.utils.ServiceLocator
import timber.log.Timber

class SpaceDeliveryApplication : Application() {

    val repository: IStationsRepository
        get() = ServiceLocator.provideStationsRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}