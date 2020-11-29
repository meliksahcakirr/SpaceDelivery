package com.meliksahcakir.spacedelivery.data

import androidx.room.ColumnInfo
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

data class Statistics(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "spentEus")
    val spentEus: Int = 0,
    @ColumnInfo(name = "deliveredUgs")
    val deliveredUgs: Int = 0,
    @ColumnInfo(name = "numberOfDestination")
    val numberOfDestination: Int = 0,
    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    @Ignore
    val gameOverReason: Int = 0
)