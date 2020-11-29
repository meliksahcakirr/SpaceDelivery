package com.meliksahcakir.spacedelivery

import android.app.Application
import com.meliksahcakir.spacedelivery.data.repository.ISpaceDeliveryRepository
import com.meliksahcakir.spacedelivery.utils.ServiceLocator
import timber.log.Timber

class SpaceDeliveryApplication : Application() {

    val repository: ISpaceDeliveryRepository
        get() = ServiceLocator.provideStationsRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}