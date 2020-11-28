package com.meliksahcakir.spacedelivery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meliksahcakir.spacedelivery.data.Station

@Database(entities = [Station::class], version = 2, exportSchema = false)
abstract class StationsDatabase : RoomDatabase() {

    abstract fun stationsDao(): StationsDao
}