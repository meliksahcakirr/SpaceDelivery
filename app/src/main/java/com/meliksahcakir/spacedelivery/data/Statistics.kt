package com.meliksahcakir.spacedelivery.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Statistics")
data class Statistics(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "spentEus")
    var spentEus: Int = 0,
    @ColumnInfo(name = "deliveredUgs")
    var deliveredUgs: Int = 0,
    @ColumnInfo(name = "numberOfDestination")
    var numberOfDestination: Int = 0,
    @ColumnInfo(name = "timestamp")
    var timestamp: Long = System.currentTimeMillis(),
    @Ignore
    var gameOverReason: Int = 0
)