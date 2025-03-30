package com.example.clima.utilites

import androidx.room.TypeConverter
import com.example.clima.model.CurrentWeather
import com.example.clima.model.ForeCast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    private val gson = Gson()

    @TypeConverter
    fun fromCurrentWeather(currentWeather: CurrentWeather): String {
        return gson.toJson(currentWeather)
    }

    @TypeConverter
    fun toCurrentWeather(data: String): CurrentWeather {
        val type = object : TypeToken<CurrentWeather>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun fromForecast(forecast: ForeCast): String {
        return gson.toJson(forecast)
    }

    @TypeConverter
    fun toForecast(data: String): ForeCast {
        val type = object : TypeToken<ForeCast>() {}.type
        return gson.fromJson(data, type)
    }

    @TypeConverter
    fun fromForecastList(value: List<ForeCast.ForecastWeather>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toForecastList(value: String): List<ForeCast.ForecastWeather>? {
        val listType = object : TypeToken<List<ForeCast.ForecastWeather>>() {}.type
        return gson.fromJson(value, listType)
    }
}