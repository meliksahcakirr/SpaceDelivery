package com.meliksahcakir.spacedelivery.data.repository

import androidx.lifecycle.LiveData
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.data.Statistics
import com.meliksahcakir.spacedelivery.data.local.ILocalDataSource
import com.meliksahcakir.spacedelivery.data.remote.IRemoteDataSource
import com.meliksahcakir.spacedelivery.data.statistics.IStatisticsSource
import com.meliksahcakir.spacedelivery.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class SpaceDeliveryRepository(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource,
    private val statisticsSource: IStatisticsSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ISpaceDeliveryRepository {

    override fun observeStations(): LiveData<Result<List<Station>>> {
        return localDataSource.observeStations()
    }

    override fun observeFavoriteStations(): LiveData<Result<List<Station>>> {
        return localDataSource.observeFavoriteStations()
    }

    override suspend fun getStations(forceUpdate: Boolean): Result<List<Station>> {
        if (forceUpdate) {
            try {
                updateStationsByRemoteDataSource()
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
        return localDataSource.getStations()
    }

    private suspend fun updateStationsByRemoteDataSource() {
        val remoteStations = remoteDataSource.getStations()
        if (remoteStations is Result.Success) {
            for (remoteStation in remoteStations.data) {
                remoteStation.currentStock = remoteStation.stock
                remoteStation.currentNeed = remoteStation.need
                val localResult = localDataSource.getStation(remoteStation.name)
                if (localResult is Result.Success) {
                    val localStation = localResult.data
                    remoteStation.favorite = localStation.favorite
                }
            }
            localDataSource.deleteStations()
            remoteStations.data.forEach { station ->
                localDataSource.addStation(station)
            }
        } else {
            localDataSource.getStations()
            localDataSource.resetStations()
            throw (remoteStations as Result.Error).exception
        }
    }

    override suspend fun refreshStations() {
        try {
            updateStationsByRemoteDataSource()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override suspend fun getStation(name: String, forceUpdate: Boolean): Result<Station> {
        if (forceUpdate) {
            try {
                updateStationsByRemoteDataSource()
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        return localDataSource.getStation(name)
    }

    override suspend fun saveStation(station: Station) {
        localDataSource.addStation(station)
    }

    override suspend fun updateStation(station: Station) {
        localDataSource.updateStation(station)
    }

    override suspend fun updateStationFavStatus(name: String, favorite: Boolean) {
        localDataSource.updateStationFavStatus(name, favorite)
    }

    override suspend fun deleteStations() {
        localDataSource.deleteStations()
    }

    override suspend fun deleteStation(name: String) {
        localDataSource.deleteStationByName(name)
    }

    override suspend fun resetStations() {
        localDataSource.resetStations()
    }

    override fun observeStatisticsList(): LiveData<Result<List<Statistics>>> {
        return statisticsSource.observeStatisticsList()
    }

    override suspend fun addStatistics(statistics: Statistics) {
        statisticsSource.addStatistics(statistics)
    }

    override suspend fun deleteStatistics(id: String) {
        statisticsSource.deleteStatistics(id)
    }
}