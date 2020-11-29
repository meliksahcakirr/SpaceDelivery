package com.meliksahcakir.spacedelivery.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meliksahcakir.spacedelivery.data.Station
import com.meliksahcakir.spacedelivery.data.Statistics
import com.meliksahcakir.spacedelivery.data.local.StationsDao
import com.meliksahcakir.spacedelivery.data.statistics.StatisticsDao

@Database(entities = [Station::class, Statistics::class], version = 3, exportSchema = false)
abstract class SpaceDeliveryDatabase : RoomDatabase() {
    abstract fun stationsDao(): StationsDao
    abstract fun statisticsDao(): StatisticsDao
}