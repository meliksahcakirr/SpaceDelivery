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

object ServiceLocator {
    private val lock = Any()
    private var database: StationsDatabase? = null

    @Volatile
    var repository: IStationsRepository? = null
        @VisibleForTesting set

    fun provideStationsRepository(context: Context): IStationsRepository {
        synchronized(this) {
            return repository ?: createStationsRepository(context)
        }
    }

    private fun createStationsRepository(context: Context): IStationsRepository {
        val repo = StationsRepository(createLocalDataSource(context), RemoteDataSource)
        repository = repo
        return repo
    }

    private fun createLocalDataSource(context: Context): ILocalDataSource {
        val database = database ?: createDatabase(context)
        return LocalDataSource(database.stationsDao())
    }

    private fun createDatabase(context: Context): StationsDatabase {
        val db = Room.databaseBuilder(
            context.applicationContext,
            StationsDatabase::class.java,
            "Stations.db"
        ).fallbackToDestructiveMigration().build()
        database = db
        return db
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            repository = null
        }
    }
}