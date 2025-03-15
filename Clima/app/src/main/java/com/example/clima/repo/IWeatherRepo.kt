package com.example.clima.repo

import com.example.clima.model.CurrentWeather

interface IWeatherRepo {
    suspend fun getCurrentWeather(): CurrentWeather

}