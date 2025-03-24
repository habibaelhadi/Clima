package com.example.clima.utilites

import androidx.annotation.DrawableRes
import com.example.clima.composableFunctions.home.view.detectIcon
import com.example.clima.model.ForeCast

data class ForecastItem(
    @DrawableRes var icon : Int,
    var dayOfWeek : String,
    var date : String,
    var temp : String,
    var airQuality : String,
    var airQualityIndicatorColor : String = "#f9cf5f",
    var isSelected : Boolean = false
)

fun ForeCast.ForecastWeather.convertAPIResponse(forecast : ForeCast.ForecastWeather,isSelected: Boolean): ForecastItem{
    return ForecastItem( icon = detectIcon(forecast.weather[0].icon) ,
    dayOfWeek = displayDate(forecast.dt.toLong(),"EEE"),
    date = displayDate(forecast.dt.toLong(),"dd MMM"),
    temp = forecast.main.temp.toInt().toString(),
    airQuality = forecast.main.feels_like.toString(),
    isSelected = isSelected)
}