package com.example.clima.repo

import com.example.clima.model.Alarm
import com.example.clima.model.CurrentWeather
import com.example.clima.model.FavouritePOJO
import com.example.clima.model.ForeCast
import com.example.clima.model.HomePOJO
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

    fun getCachedHome() : Flow<HomePOJO>
    suspend fun insertCacheHome(home : HomePOJO)

    fun getLocationSourceFlow(): Flow<String>
    fun getLocationChange(): Flow<Pair<String, String>>

    fun setLocationSource(source: String)
    fun getLocationSource(): String

    fun setSplashState(state:Boolean)
    fun getSplashState():Boolean

    fun setTemperatureUnit(unit: String)
    fun getTemperatureUnit(): String

    fun setWindSpeedUnit(unit: String)
    fun getWindSpeedUnit(): String

    fun setLanguage(language: String)
    fun getLanguage(): String

    fun setMapCoordinates(lat: String, lon: String)
    fun getMapCoordinates(): Pair<String, String>

    fun clearPreferences()
}