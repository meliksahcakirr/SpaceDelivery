package com.meliksahcakir.spacedelivery.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.meliksahcakir.spacedelivery.data.Station

@Dao
interface StationsDao {

    @Query("SELECT * FROM Stations")
    fun observeStations(): LiveData<List<Station>>

    @Query("SELECT * FROM Stations")
    suspend fun getStations(): List<Station>

    @Query("SELECT * FROM Stations WHERE name = :name")
    suspend fun getStationByName(name: String): Station?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStation(station: Station)

    @Update
    suspend fun updateStation(station: Station): Int

    @Query("UPDATE Stations SET favorite = :favorite WHERE name = :name")
    suspend fun updateFavStatus(name: String, favorite: Boolean)

    @Query("DELETE FROM Stations")
    suspend fun deleteStations()

    @Query("DELETE FROM Stations WHERE name = :name")
    suspend fun deleteStationByName(name: String)
}