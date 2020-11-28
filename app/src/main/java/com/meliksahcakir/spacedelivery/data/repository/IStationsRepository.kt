package com.meliksahcakir.spacedelivery.data.repository

import androidx.lifecycle.LiveData
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.utils.Result

interface IStationsRepository {

    fun observeStations(): LiveData<Result<List<Station>>>

    suspend fun getStations(forceUpdate: Boolean = false): Result<List<Station>>

    suspend fun refreshStations()

    suspend fun getStation(name: String, forceUpdate: Boolean = false): Result<Station>

    suspend fun saveStation(station: Station)

    suspend fun updateStation(station: Station)

    suspend fun updateStationFavStatus(name: String, favorite: Boolean)

    suspend fun deleteStations()

    suspend fun deleteStation(name: String)
}