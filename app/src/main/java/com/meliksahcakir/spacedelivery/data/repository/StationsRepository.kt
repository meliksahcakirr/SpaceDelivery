package com.meliksahcakir.spacedelivery.data.repository

import androidx.lifecycle.LiveData
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.data.local.ILocalDataSource
import com.meliksahcakir.spacedelivery.data.remote.IRemoteDataSource
import com.meliksahcakir.spacedelivery.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class StationsRepository(
    private val localDataSource: ILocalDataSource,
    private val remoteDataSource: IRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IStationsRepository {

    override fun observeStations(): LiveData<Result<List<Station>>> {
        return localDataSource.observeStations()
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
            throw (remoteStations as Result.Error).exception
        }
    }

    override suspend fun refreshStations() {
        updateStationsByRemoteDataSource()
    }

    override suspend fun getStation(name: String, forceUpdate: Boolean): Result<Station> {
        if (forceUpdate) {
            updateStationsByRemoteDataSource()
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
}