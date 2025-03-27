package com.example.clima.local

import com.example.clima.model.FavouritePOJO
import kotlinx.coroutines.flow.Flow

class WeatherLocalDataSource(private val weatherDao: WeatherDao) : IWeatherLocalDataSource {
    override suspend fun insertWeather(weather: FavouritePOJO) {
        weatherDao.insertWeather(weather)
    }

    override suspend fun deleteWeather(weather: FavouritePOJO) {
        weatherDao.deleteWeather(weather)
    }

    override fun getWeather(): Flow<List<FavouritePOJO>> {
        return weatherDao.getWeather()
    }
}