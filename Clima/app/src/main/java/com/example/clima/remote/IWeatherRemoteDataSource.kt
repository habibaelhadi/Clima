package com.example.clima.remote

import com.example.clima.model.CurrentWeather
import kotlinx.coroutines.flow.Flow

interface IWeatherRemoteDataSource {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units : String,
        lang : String
    ): Flow<CurrentWeather>
}