package com.meliksahcakir.spacedelivery.data.local

import androidx.lifecycle.LiveData
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.utils.Result

interface ILocalDataSource {

    fun observeStations(): LiveData<Result<List<Station>>>

    suspend fun getStations(): Result<List<Station>>

    suspend fun getStation(name: String): Result<Station>

    suspend fun addStation(station: Station)

    suspend fun updateStation(station: Station)

    suspend fun updateStationFavStatus(name: String, favorite: Boolean)

    suspend fun deleteStations()

    suspend fun deleteStationByName(name: String)
}