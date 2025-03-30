package com.example.clima.local

import com.example.clima.model.Alarm
import com.example.clima.model.FavouritePOJO
import kotlinx.coroutines.flow.Flow

interface IWeatherLocalDataSource {
    suspend fun insertWeather(weather: FavouritePOJO)
    suspend fun deleteWeather(weather: FavouritePOJO)
    fun getWeather(): Flow<List<FavouritePOJO>>
    fun getAlarms(): Flow<List<Alarm>>
    suspend fun getAlarm(alarmId: Int): Alarm?
    suspend fun insertAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
}