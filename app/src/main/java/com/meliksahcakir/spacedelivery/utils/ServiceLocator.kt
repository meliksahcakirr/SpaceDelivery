package com.meliksahcakir.spacedelivery.utils

import android.content.Context
import androidx.room.Room
import com.meliksahcakir.spacedelivery.data.local.ILocalDataSource
import com.meliksahcakir.spacedelivery.data.local.LocalDataSource
import com.meliksahcakir.spacedelivery.data.remote.RemoteDataSource
import com.meliksahcakir.spacedelivery.data.repository.ISpaceDeliveryRepository
import com.meliksahcakir.spacedelivery.data.repository.SpaceDeliveryDatabase
import com.meliksahcakir.spacedelivery.data.repository.SpaceDeliveryRepository
import com.meliksahcakir.spacedelivery.data.statistics.IStatisticsSource
import com.meliksahcakir.spacedelivery.data.statistics.StatisticsSource

object ServiceLocator {
    private val lock = Any()
    private var database: SpaceDeliveryDatabase? = null

    @Volatile
    var repository: ISpaceDeliveryRepository? = null
    //@VisibleForTesting set

    fun provideSpaceDeliveryRepository(context: Context): ISpaceDeliveryRepository {
        synchronized(this) {
            return repository ?: createSpaceDeliveryRepository(context)
        }
    }

    private fun createSpaceDeliveryRepository(context: Context): ISpaceDeliveryRepository {
        val repo = SpaceDeliveryRepository(
            createLocalDataSource(context),
            RemoteDataSource,
            createStatisticsSource(context)
        )
        repository = repo
        return repo
    }

    private fun createLocalDataSource(context: Context): ILocalDataSource {
        val database = database ?: createDatabase(context)
        return LocalDataSource(database.stationsDao())
    }

    private fun createDatabase(context: Context): SpaceDeliveryDatabase {
        val db = Room.databaseBuilder(
            context.applicationContext,
            SpaceDeliveryDatabase::class.java,
            "SpaceDelivery.db"
        ).build()
        database = db
        return db
    }

    private fun createStatisticsSource(context: Context): IStatisticsSource {
        val database = database ?: createDatabase(context)
        return StatisticsSource(database.statisticsDao())
    }

    /*@VisibleForTesting
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
    }*/
}