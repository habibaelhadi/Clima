package com.example.clima.model

import androidx.room.Entity

@Entity(tableName = "weather_table",
    primaryKeys = ["longitude", "latitude"])
data class DataBaseTable (
    val longitude : Double,
    val latitude : Double
)