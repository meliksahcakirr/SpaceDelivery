package com.meliksahcakir.spacedelivery.data.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.utils.Result
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface StationService {
    @GET("e7211664-cbb6-4357-9c9d-f12bf8bab2e2")
    fun getStationsAsync(): Deferred<List<Station>>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object RemoteDataSource : IRemoteDataSource {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://run.mocky.io/v3/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    private val service = retrofit.create(StationService::class.java)

    override suspend fun getStations(): Result<List<Station>> {
        return withContext(Dispatchers.IO) {
            try {
                val stations = service.getStationsAsync().await()
                Result.Success(stations)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

}