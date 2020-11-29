package com.meliksahcakir.spacedelivery.data.statistics

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meliksahcakir.spacedelivery.data.Statistics

@Database(entities = [Statistics::class], version = 1, exportSchema = false)
abstract class StatisticsDatabase : RoomDatabase() {

    abstract fun statisticsDao(): StatisticsDao
}