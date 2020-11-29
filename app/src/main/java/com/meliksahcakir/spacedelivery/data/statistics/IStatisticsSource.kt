package com.meliksahcakir.spacedelivery.data.statistics

import androidx.lifecycle.LiveData
import com.meliksahcakir.spacedelivery.data.Statistics
import com.meliksahcakir.spacedelivery.utils.Result

interface IStatisticsSource {

    fun observeStatisticsList(): LiveData<Result<List<Statistics>>>

    suspend fun getStatisticsList(): Result<List<Statistics>>

    suspend fun getStatistics(id: String): Result<Statistics>

    suspend fun addStatistics(statistics: Statistics)

    suspend fun updateStatistics(statistics: Statistics)

    suspend fun deleteStatistics(id: String)

    suspend fun deleteStatisticsList()
}