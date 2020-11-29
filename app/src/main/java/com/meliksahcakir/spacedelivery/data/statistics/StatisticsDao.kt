package com.meliksahcakir.spacedelivery.data.statistics

import androidx.lifecycle.LiveData
import androidx.room.*
import com.meliksahcakir.spacedelivery.data.Statistics

@Dao
interface StatisticsDao {

    @Query("SELECT * FROM Statistics")
    fun observeStatisticsList(): LiveData<List<Statistics>>

    @Query("SELECT * FROM Statistics")
    suspend fun getStatisticsList(): List<Statistics>

    @Query("SELECT * FROM Statistics WHERE id = :id")
    suspend fun getStatistics(id: String): Statistics?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStatistics(statistics: Statistics)

    @Update
    suspend fun updateStatistics(statistics: Statistics)

    @Query("DELETE FROM Statistics WHERE id = :id")
    suspend fun deleteStatistics(id: String)

    @Query("DELETE FROM Stations")
    suspend fun deleteStatisticsList()
}