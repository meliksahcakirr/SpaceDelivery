package com.meliksahcakir.spacedelivery.data.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.meliksahcakir.spacedelivery.data.Statistics
import com.meliksahcakir.spacedelivery.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StatisticsSource internal constructor(
    private val statisticsDao: StatisticsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IStatisticsSource {

    override fun observeStatisticsList(): LiveData<Result<List<Statistics>>> {
        return statisticsDao.observeStatisticsList().map {
            Result.Success(it)
        }
    }

    override suspend fun getStatisticsList(): Result<List<Statistics>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(statisticsDao.getStatisticsList())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getStatistics(id: String): Result<Statistics> = withContext(ioDispatcher) {
        return@withContext try {
            val statistics = statisticsDao.getStatistics(id)
            if (statistics != null) {
                Result.Success(statistics)
            } else {
                Result.Error(Exception("no data"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addStatistics(statistics: Statistics) {
        statisticsDao.insertStatistics(statistics)
    }

    override suspend fun updateStatistics(statistics: Statistics) {
        statisticsDao.updateStatistics(statistics)
    }

    override suspend fun deleteStatistics(id: String) {
        statisticsDao.deleteStatistics(id)
    }

    override suspend fun deleteStatisticsList() {
        statisticsDao.deleteStatisticsList()
    }
}