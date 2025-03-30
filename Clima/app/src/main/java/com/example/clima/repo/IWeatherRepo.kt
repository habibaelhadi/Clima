package com.example.clima.repo

import com.example.clima.model.Alarm
import com.example.clima.model.CurrentWeather
import com.example.clima.model.FavouritePOJO
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

    suspend fun insertWeather(weather: FavouritePOJO)
    suspend fun deleteWeather(weather: FavouritePOJO)
    fun getWeather(): Flow<List<FavouritePOJO>>

    suspend fun insertAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
    fun getAlarms(): Flow<List<Alarm>>
    suspend fun getAlarm(alarmId: Int): Alarm?
}