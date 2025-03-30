package com.example.clima.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.clima.utilites.Converter


@Entity(tableName = "home_table")
data class HomePOJO(
    @PrimaryKey
    val id : Int = 1,
    val currentWeather: CurrentWeather,
    @TypeConverters(Converter::class)
    val foreCast: List<ForeCast. ForecastWeather>,
    @TypeConverters(Converter::class)
    val hours : List<ForeCast. ForecastWeather>
)