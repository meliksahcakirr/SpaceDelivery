package com.meliksahcakir.spacedelivery.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.utils.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDataSource internal constructor(
    private val stationsDao: StationsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ILocalDataSource {

    override fun observeStations(): LiveData<Result<List<Station>>> {
        return stationsDao.observeStations().map {
            Result.Success(it)
        }
    }

    override fun observeFavoriteStations(): LiveData<Result<List<Station>>> {
        return stationsDao.observeFavoriteStations().map {
            Result.Success(it)
        }
    }

    override suspend fun getStations(): Result<List<Station>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(stationsDao.getStations())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getStation(name: String): Result<Station> = withContext(ioDispatcher) {
        return@withContext try {
            val station = stationsDao.getStationByName(name)
            if (station != null) {
                Result.Success(station)
            } else {
                Result.Error(Exception("no data"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addStation(station: Station) {
        stationsDao.insertStation(station)
    }

    override suspend fun updateStation(station: Station) {
        stationsDao.updateStation(station)
    }

    override suspend fun updateStationFavStatus(name: String, favorite: Boolean) {
        stationsDao.updateFavStatus(name, favorite)
    }

    override suspend fun deleteStations() {
        stationsDao.deleteStations()
    }

    override suspend fun deleteStationByName(name: String) {
        stationsDao.deleteStationByName(name)
    }

    override suspend fun resetStations() {
        stationsDao.resetStations()
    }
}