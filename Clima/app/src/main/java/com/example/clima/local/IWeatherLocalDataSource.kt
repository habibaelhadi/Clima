package com.example.clima.local

import com.example.clima.model.DataBaseTable
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    suspend fun insertWeather(weather: DataBaseTable)
    suspend fun deleteWeather(weather: DataBaseTable)
    fun getWeather(): Flow<List<DataBaseTable>>
}