package com.example.clima.local

import android.content.Context
import android.content.SharedPreferences
import com.example.clima.R
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class SharedPreferencesDataSource private constructor(val context : Context) : ISharedPreferencesDataSource {
    private val prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        @Volatile
        private var instance: SharedPreferencesDataSource? = null

        fun getInstance(context: Context): SharedPreferencesDataSource {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferencesDataSource(context.applicationContext).also { instance = it }
            }
        }
    }

    override fun getLocationSourceFlow(): Flow<String> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "app_location") {
                trySend(getLocationSource())
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(getLocationSource())
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override fun getLocationChange(): Flow<Pair<String, String>> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "lat" || key == "lng") {
                trySend(getMapCoordinates())
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(getMapCoordinates())
        awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override fun setLocationSource(source: String) {
        prefs.edit()
            .putString("app_location", source)
            .apply()
    }

    override fun getLocationSource(): String {
        val value = prefs.getString("app_location", context.getString(R.string.gps))
        return value ?: context.getString(R.string.gps)
    }

    override fun setTemperatureUnit(unit: String) {
        prefs.edit()
            .putString("app_temp", unit)
            .apply()
    }

    override fun getTemperatureUnit(): String {
        val value = prefs.getString("app_temp", context.getString(R.string.celsius_c))
        return value ?: context.getString(R.string.celsius_c)
    }

    override fun setWindSpeedUnit(unit: String) {
        prefs.edit()
            .putString("app_wind", unit)
            .apply()
    }

    override fun getWindSpeedUnit(): String {
        val value = prefs.getString("app_wind", context.getString(R.string.miles_per_hour_m_h))
        return value ?: context.getString(R.string.miles_per_hour_m_h)
    }

    override fun setLanguage(language: String) {
        prefs.edit()
            .putString("app_language", language)
            .apply()
    }

    override fun getLanguage(): String {
        val value = prefs.getString("app_language", "English")
        return value ?: "English"
    }

    override fun setMapCoordinates(lat: String, lon: String) {
        prefs.edit()
            .putString("lat", lat)
            .putString("lng", lon)
            .apply()
    }

    override fun getMapCoordinates(): Pair<String, String> {
        val lat = prefs.getString("lat", "0.0") ?: "0.0"
        val lon = prefs.getString("lng", "0.0") ?: "0.0"
        return Pair(lat, lon)
    }

    override fun clearPreferences() {
        prefs.edit().clear().apply()
    }

}