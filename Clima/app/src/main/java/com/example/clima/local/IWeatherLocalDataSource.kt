package com.example.clima.local

import com.example.clima.model.FavouritePOJO
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    suspend fun insertWeather(weather: FavouritePOJO)
    suspend fun deleteWeather(weather: FavouritePOJO)
    fun getWeather(): Flow<List<FavouritePOJO>>
}