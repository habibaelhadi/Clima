package com.example.clima.repo

import com.example.clima.local.IWeatherLocalDataSource
import com.example.clima.model.Alarm
import com.example.clima.model.CurrentWeather
import com.example.clima.model.FavouritePOJO
import com.example.clima.model.ForeCast
import com.example.clima.model.HomePOJO
import com.example.clima.remote.IWeatherRemoteDataSource
import com.example.clima.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepo private constructor(
    private val weatherRemoteDataSource: IWeatherRemoteDataSource,
    private val weatherLocalDataSource: IWeatherLocalDataSource
) : IWeatherRepo  {

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units : String,
        lang : String
    ): Flow<CurrentWeather> {
        return weatherRemoteDataSource.getCurrentWeather(latitude,longitude,units,lang)
    }

    override suspend fun getForecast(
        latitude: Double,
        longitude: Double,
        units: String,
        lang: String
    ): Flow<ForeCast> {
        return weatherRemoteDataSource.getForecast(latitude,longitude,units,lang)
    }

    override suspend fun insertWeather(weather: FavouritePOJO) {
        weatherLocalDataSource.insertWeather(weather)
    }

    override suspend fun deleteWeather(weather: FavouritePOJO) {
        weatherLocalDataSource.deleteWeather(weather)
    }

    override fun getWeather(): Flow<List<FavouritePOJO>> {
        return weatherLocalDataSource.getWeather()
    }

    override suspend fun insertAlarm(alarm: Alarm) {
        weatherLocalDataSource.insertAlarm(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        weatherLocalDataSource.deleteAlarm(alarm)
    }

    override fun getAlarms(): Flow<List<Alarm>> {
        return weatherLocalDataSource.getAlarms()
    }

    override suspend fun getAlarm(alarmId: Int): Alarm? {
        return weatherLocalDataSource.getAlarm(alarmId)
    }

    override fun getCachedHome(): Flow<HomePOJO> {
        return weatherLocalDataSource.getCachedHome()
    }

    override suspend fun insertCacheHome(home: HomePOJO) {
        weatherLocalDataSource.insertCacheHome(home)
    }

    companion object {
        @Volatile
        private var instance: WeatherRepo? = null
        fun getInstance(
            remoteDataSource: WeatherRemoteDataSource,
            localDataSource: IWeatherLocalDataSource
        ): WeatherRepo {
            return instance ?: synchronized(this) {
                instance ?: WeatherRepo(remoteDataSource,localDataSource).also { instance = it }
            }
        }
    }
}