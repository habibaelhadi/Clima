package com.example.clima.utilites

import android.content.Context
import androidx.annotation.DrawableRes
import com.example.clima.R

data class AirQualityItem (
    @DrawableRes val icon : Int,
    val title : String,
    var value : String
)

fun getAirItems(context: Context): List<AirQualityItem> {
    return listOf(
        AirQualityItem(
            icon = R.drawable.wind,
            title = context.getString(R.string.wind_speed),
            value = "9km/h"
        ),
        AirQualityItem(
            icon = R.drawable.humidity,
            title = context.getString(R.string.humidity),
            value = "50%"
        ),
        AirQualityItem(
            icon = R.drawable.air_pressure,
            title = context.getString(R.string.air_pressure),
            value = "1016 hPa"
        ),
        AirQualityItem(
            icon = R.drawable.clouds,
            title = context.getString(R.string.clouds),
            value = "9km/h"
        )
    )
}