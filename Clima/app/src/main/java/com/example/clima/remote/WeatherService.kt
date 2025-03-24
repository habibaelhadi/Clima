package com.example.clima.remote

import com.example.clima.BuildConfig
import com.example.clima.model.CurrentWeather
import com.example.clima.model.ForeCast
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("data/2.5/weather")
    suspend fun getWeatherData(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("lang") language: String,
        @Query("appid") apiKey: String = BuildConfig.apiKeySafe
    ): CurrentWeather

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("lang") language: String,
        @Query("appid") apiKey: String = BuildConfig.apiKeySafe
    ) : ForeCast
}