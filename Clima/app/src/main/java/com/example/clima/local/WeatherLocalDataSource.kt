package com.example.clima.local

import com.example.clima.model.DataBaseTable
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private val weatherDao: WeatherDao) : IWeatherLocalDataSource {
    override suspend fun insertWeather(weather: DataBaseTable) {
        weatherDao.insertWeather(weather)
    }

    override suspend fun deleteWeather(weather: DataBaseTable) {
        weatherDao.deleteWeather(weather)
    }

    override fun getWeather(): Flow<List<DataBaseTable>> {
        return weatherDao.getWeather()
    }
}