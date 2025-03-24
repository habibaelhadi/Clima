package com.example.clima.repo

import com.example.clima.model.CurrentWeather
import com.example.clima.model.ForeCast
import com.example.clima.remote.IWeatherRemoteDataSource
import com.example.clima.remote.WeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepo private constructor(private val weatherRemoteDataSource: IWeatherRemoteDataSource
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

    companion object {
        @Volatile
        private var instance: WeatherRepo? = null
        fun getInstance(remoteDataSource: WeatherRemoteDataSource): WeatherRepo {
            return instance ?: synchronized(this) {
                instance ?: WeatherRepo(remoteDataSource).also { instance = it }
            }
        }
    }
}