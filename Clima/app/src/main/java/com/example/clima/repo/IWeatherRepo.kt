package com.example.clima.repo

import com.example.clima.model.CurrentWeather
import com.example.clima.model.DataBaseTable
import com.example.clima.model.ForeCast
import kotlinx.coroutines.flow.Flow

interface IWeatherRepo {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units : String,
        lang : String
    ): Flow<CurrentWeather>

    suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        units : String,
        lang : String
    ) : Flow<ForeCast>

    suspend fun insertWeather(weather: DataBaseTable)
    suspend fun deleteWeather(weather: DataBaseTable)
    fun getWeather(): Flow<List<DataBaseTable>>
}