package com.example.clima.local

import com.example.clima.model.Alarm
import com.example.clima.model.FavouritePOJO
import com.example.clima.model.HomePOJO
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

    override fun getAlarms(): Flow<List<Alarm>> {
        return weatherDao.getAlarms()
    }

    override suspend fun getAlarm(alarmId: Int): Alarm? {
        return weatherDao.getAlarm(alarmId)
    }

    override suspend fun insertAlarm(alarm : Alarm) {
       return weatherDao.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        return weatherDao.deleteAlarm(alarm)
    }

    override fun getCachedHome(): Flow<HomePOJO> {
        return weatherDao.getCachedHome()
    }

    override suspend fun insertCacheHome(home: HomePOJO) {
        weatherDao.insertCacheHome(home)
    }
}