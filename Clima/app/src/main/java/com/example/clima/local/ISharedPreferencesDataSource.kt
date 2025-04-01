package com.example.clima.local

import kotlinx.coroutines.flow.Flow

interface ISharedPreferencesDataSource {
    fun getLocationSourceFlow(): Flow<String>
    fun getLocationChange(): Flow<Pair<String, String>>

    fun setLocationSource(source: String)
    fun getLocationSource(): String

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