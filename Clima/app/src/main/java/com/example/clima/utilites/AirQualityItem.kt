package com.example.clima.utilites

import androidx.annotation.DrawableRes
import com.example.clima.R

data class AirQualityItem (
    @DrawableRes val icon : Int,
    val title : String,
    val value : String
)

val airItems = mutableListOf(
    AirQualityItem( // Wind speed
         icon = R.drawable.wind,
        title = "Wind Speed",
        value = "9km/h"),

    AirQualityItem( // Humidity
        icon = R.drawable.humidity,
        title = "Humidity",
        value = "50%"),

    AirQualityItem( // Air Pressure
        icon = R.drawable.air_pressure,
        title = "Air Pressure",
        value = "1016 hPa"),

    AirQualityItem( // Clouds
        icon = R.drawable.clouds,
        title = "Clouds",
        value = "9km/h")
)