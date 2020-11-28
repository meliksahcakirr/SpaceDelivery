package com.meliksahcakir.spacedelivery.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.hypot

@Entity(tableName = "Stations")
data class Station(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "coordinateX")
    val coordinateX: Double = 0.0,
    @ColumnInfo(name = "coordinateY")
    val coordinateY: Double = 0.0,
    @ColumnInfo(name = "capacity")
    val capacity: Int = 0,
    @ColumnInfo(name = "stock")
    var stock: Int = 0,
    @ColumnInfo(name = "need")
    var need: Int = 0,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean = false,
    @ColumnInfo(name = "currentStock")
    var currentStock: Int = 0,
    @ColumnInfo(name = "currentNeed")
    var currentNeed: Int = 0,
    @ColumnInfo(name = "completed")
    var completed: Boolean = false
) {

    companion object {
        val EARTH = Station("Dünya", 0.0, 0.0, 0, 0, 0)
    }

    fun deliverPackets(numberOfPackets: Int): Int {
        var remaining = 0
        if (numberOfPackets > currentNeed) {
            remaining = numberOfPackets - currentNeed
            currentStock += currentNeed
            currentNeed = 0
        } else {
            currentStock += numberOfPackets
            currentNeed -= numberOfPackets
        }
        return remaining
    }

    fun calculateEus(other: Station): Int {
        return hypot(coordinateX - other.coordinateX, coordinateY - other.coordinateY).toInt()
    }
}

