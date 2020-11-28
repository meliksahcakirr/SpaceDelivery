package com.meliksahcakir.spacedelivery.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
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
    var need: Int = 0
) {

    companion object {
        val EARTH = Station("Dünya", 0.0, 0.0, 0, 0, 0)
    }

    fun deliverPackets(numberOfPackets: Int): Int {
        var remaining = 0
        if (numberOfPackets > need) {
            remaining = numberOfPackets - need
            stock += need
            need = 0
        } else {
            stock += numberOfPackets
            need -= numberOfPackets
        }
        return remaining
    }
}

