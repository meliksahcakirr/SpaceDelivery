package com.meliksahcakir.spacedelivery.main

import androidx.lifecycle.ViewModel
import com.meliksahcakir.spacedelivery.data.Shuttle

class MainViewModel : ViewModel() {

    private var shuttle: Shuttle? = null

    fun onShuttleSelected(shuttle: Shuttle) {
        this.shuttle = shuttle
    }
}