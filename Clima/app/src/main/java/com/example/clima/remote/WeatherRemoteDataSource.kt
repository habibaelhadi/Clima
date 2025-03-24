package com.example.clima.remote

import com.example.clima.BuildConfig
import com.example.clima.model.CurrentWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class WeatherRemoteDataSource(private val weatherService: WeatherService) : IWeatherRemoteDataSource  {

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units : String,
        lang : String
    ): Flow<CurrentWeather> {
        return flowOf(weatherService.getWeatherData(latitude,longitude,units,lang))
    }
}