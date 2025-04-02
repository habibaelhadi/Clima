package com.example.clima.repo

import com.example.clima.local.IWeatherLocalDataSource
import com.example.clima.local.SharedPreferencesDataSource
import com.example.clima.model.Alarm
import com.example.clima.model.CurrentWeather
import com.example.clima.model.FavouritePOJO
import com.example.clima.model.ForeCast
import com.example.clima.model.HomePOJO
import com.example.clima.remote.IWeatherRemoteDataSource
import kotlinx.coroutines.flow.Flow

class WeatherRepo private constructor(
    private val weatherRemoteDataSource: IWeatherRemoteDataSource,
    private val weatherLocalDataSource: IWeatherLocalDataSource,
    private val sharedPreferencesDataSource: SharedPreferencesDataSource
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

    override fun getLocationSourceFlow(): Flow<String> {
        return sharedPreferencesDataSource.getLocationSourceFlow()
    }

    override fun getLocationChange(): Flow<Pair<String, String>> {
        return sharedPreferencesDataSource.getLocationChange()
    }

    override fun setLocationSource(source: String) {
        sharedPreferencesDataSource.setLocationSource(source)
    }

    override fun getLocationSource(): String {
        return sharedPreferencesDataSource.getLocationSource()
    }

    override fun setSplashState(state: Boolean) {
        sharedPreferencesDataSource.setSplashState(state)
    }

    override fun getSplashState(): Boolean {
        return sharedPreferencesDataSource.getSplashState()
    }

    override fun setTemperatureUnit(unit: String) {
       sharedPreferencesDataSource.setTemperatureUnit(unit)
    }

    override fun getTemperatureUnit(): String {
        return sharedPreferencesDataSource.getTemperatureUnit()
    }

    override fun setWindSpeedUnit(unit: String) {
        sharedPreferencesDataSource.setWindSpeedUnit(unit)
    }

    override fun getWindSpeedUnit(): String {
       return sharedPreferencesDataSource.getWindSpeedUnit()
    }

    override fun setLanguage(language: String) {
        sharedPreferencesDataSource.setLanguage(language)
    }

    override fun getLanguage(): String {
        return sharedPreferencesDataSource.getLanguage()
    }

    override fun setMapCoordinates(lat: String, lon: String) {
        sharedPreferencesDataSource.setMapCoordinates(lat,lon)
    }

    override fun getMapCoordinates(): Pair<String, String> {
        return sharedPreferencesDataSource.getMapCoordinates()
    }

    override fun clearPreferences() {
       sharedPreferencesDataSource.clearPreferences()
    }

    companion object {
        @Volatile
        private var instance: WeatherRepo? = null
        fun getInstance(
            remoteDataSource: IWeatherRemoteDataSource,
            localDataSource: IWeatherLocalDataSource,
            sharedPreferencesDataSource: SharedPreferencesDataSource
        ): WeatherRepo {
            return instance ?: synchronized(this) {
                instance ?: WeatherRepo(remoteDataSource,localDataSource,sharedPreferencesDataSource).also { instance = it }
            }
        }
    }
}