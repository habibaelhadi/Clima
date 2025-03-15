package com.example.clima.remote

import com.example.clima.model.CurrentWeather

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(): CurrentWeather
}