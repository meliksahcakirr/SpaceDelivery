package com.meliksahcakir.spacedelivery

import android.app.Application
import timber.log.Timber

class SpaceDeliveryApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}