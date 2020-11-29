package com.meliksahcakir.spacedelivery.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.meliksahcakir.spacedelivery.data.Statistics
import com.meliksahcakir.spacedelivery.data.repository.ISpaceDeliveryRepository
import com.meliksahcakir.spacedelivery.utils.Result

class StatisticsViewModel(repository: ISpaceDeliveryRepository) : ViewModel() {

    val statisticsList: LiveData<List<Statistics>> = repository.observeStatisticsList().map {
        if (it is Result.Success) {
            it.data
        } else {
            emptyList()
        }
    }


}