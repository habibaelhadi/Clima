package com.example.clima.model

import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.clima.utilites.Converter

@TypeConverters(Converter::class)
@Entity(tableName = "weather_table",
    primaryKeys = ["longitude", "latitude"])
data class FavouritePOJO (
    val latitude : Double,
    val longitude : Double,
    val currentWeather : CurrentWeather,
    val forecast : ForeCast,
    val country : String,
    val city : String
)