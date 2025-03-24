package com.example.clima.utilites

import androidx.compose.ui.graphics.Color
import com.example.clima.model.ForeCast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun displayDate(date: Long, pattern: String): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(date * 1000)
}

fun ForeCast.dailyForecasts(): Map<Int, List<ForeCast.ForecastWeather>> {
    val forecastMap = mutableMapOf<Int, MutableList<ForeCast.ForecastWeather>>()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    for (item in list) {
        val dateKey = dateFormat.format(Date(item.dt * 1000L)).replace("-", "").toInt()
        if (forecastMap[dateKey] == null) {
            forecastMap[dateKey] = mutableListOf()
        }
        forecastMap[dateKey]?.add(item)
    }
    return forecastMap.mapValues { it.value.take(8) }
}

fun Color.Companion.fromHex(hex: String): Color =
    Color(android.graphics.Color.parseColor(hex))