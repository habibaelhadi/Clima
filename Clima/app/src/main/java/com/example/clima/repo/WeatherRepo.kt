package com.example.clima.repo

import com.example.clima.model.CurrentWeather
import com.example.clima.remote.IWeatherRemoteDataSource
import com.example.clima.remote.WeatherRemoteDataSource

class WeatherRepo private constructor(private val weatherRemoteDataSource: IWeatherRemoteDataSource
) : IWeatherRepo  {

    override suspend fun getCurrentWeather(): CurrentWeather {
        return weatherRemoteDataSource.getCurrentWeather()
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