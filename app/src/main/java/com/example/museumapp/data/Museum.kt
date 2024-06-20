package com.example.tricitytour.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "museums")
data class Museum (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
//    @ColumnInfo(name = "name")
    var name: String,
//    @ColumnInfo(name = "address")
    var address: String,
//    @ColumnInfo(name = "theme")
    var theme: String,
//    @ColumnInfo(name = "visiting_time")
    var avgVisitingHours: Double,
//    @ColumnInfo(name = "longitude")
    var longitude: Double,
//    @ColumnInfo(name = "latitude")
    var latitude: Double,
    var seen: Boolean,
    var seenDate: LocalDate?
)