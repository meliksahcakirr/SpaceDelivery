package com.meliksahcakir.spacedelivery.utils

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.meliksahcakir.spacedelivery.data.local.ILocalDataSource
import com.meliksahcakir.spacedelivery.data.local.LocalDataSource
import com.meliksahcakir.spacedelivery.data.local.StationsDatabase
import com.meliksahcakir.spacedelivery.data.remote.RemoteDataSource
import com.meliksahcakir.spacedelivery.data.repository.IStationsRepository
import com.meliksahcakir.spacedelivery.data.repository.StationsRepository
import com.meliksahcakir.spacedelivery.data.statistics.IStatisticsSource
import com.meliksahcakir.spacedelivery.data.statistics.StatisticsDatabase
import com.meliksahcakir.spacedelivery.data.statistics.StatisticsSource

object ServiceLocator {
    private val lock = Any()
    private var stationsDatabase: StationsDatabase? = null
    private var statisticsDatabase: StatisticsDatabase? = null

    @Volatile
    var repository: IStationsRepository? = null
        @VisibleForTesting set

    fun provideStationsRepository(context: Context): IStationsRepository {
        synchronized(this) {
            return repository ?: createStationsRepository(context)
        }
    }

    private fun createStationsRepository(context: Context): IStationsRepository {
        val repo = StationsRepository(
            createLocalDataSource(context),
            RemoteDataSource,
            createStatisticsSource(context)
        )
        repository = repo
        return repo
    }

    private fun createLocalDataSource(context: Context): ILocalDataSource {
        val database = stationsDatabase ?: createStationsDatabase(context)
        return LocalDataSource(database.stationsDao())
    }

    private fun createStationsDatabase(context: Context): StationsDatabase {
        val db = Room.databaseBuilder(
            context.applicationContext,
            StationsDatabase::class.java,
            "Stations.db"
        ).fallbackToDestructiveMigration().build()
        stationsDatabase = db
        return db
    }

    private fun createStatisticsSource(context: Context): IStatisticsSource {
        val database = statisticsDatabase ?: createStatisticsDatabase(context)
        return StatisticsSource(database.statisticsDao())
    }

    private fun createStatisticsDatabase(context: Context): StatisticsDatabase {
        val db = Room.databaseBuilder(
            context.applicationContext,
            StatisticsDatabase::class.java,
            "Stations.db"
        ).fallbackToDestructiveMigration().build()
        statisticsDatabase = db
        return db
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            stationsDatabase?.apply {
                clearAllTables()
                close()
            }
            statisticsDatabase?.apply {
                clearAllTables()
                close()
            }
            stationsDatabase = null
            statisticsDatabase = null
            repository = null
        }
    }
}