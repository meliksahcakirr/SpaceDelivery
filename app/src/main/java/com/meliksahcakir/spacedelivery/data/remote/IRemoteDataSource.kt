package com.meliksahcakir.spacedelivery.data.remote

import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.utils.Result

interface IRemoteDataSource {

    suspend fun getStations(): Result<List<Station>>
}