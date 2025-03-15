package com.example.clima.remote

import com.example.clima.BuildConfig
import com.example.clima.model.CurrentWeather



class WeatherRemoteDataSource(private val weatherService: WeatherService) : IWeatherRemoteDataSource  {

    override suspend fun getCurrentWeather(): CurrentWeather {
        return weatherService.getWeatherData(44.34,10.99, BuildConfig.apiKeySafe).body()!!
    }
}