package com.example.clima.composable.alarms.broadcastrecievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.clima.local.AppDataBase
import com.example.clima.local.WeatherLocalDataSource
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import com.example.clima.utilites.createNotification
import com.example.clima.utilites.getLanguageCode
import com.example.clima.utilites.playAudio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        if (alarmId == -1) return

        val repository = WeatherRepo.getInstance(
            WeatherRemoteDataSource(RetrofitProduct.retrofit),
            WeatherLocalDataSource(AppDataBase.getInstance(context).weatherDao())
        )

        CoroutineScope(Dispatchers.IO).launch {
            val alarm = repository.getAlarm(alarmId) ?: return@launch

            val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val savedLanguage = sharedPreferences.getString("app_language", "English") ?: "English"
            val language = getLanguageCode(savedLanguage).toString()

            val weatherFlow = repository.getCurrentWeather(50.0, 70.0, "metric", language)
            var weatherDescription = "Weather data not available"

            weatherFlow.collect { weatherResponse ->
               weatherResponse.weather.let {
                   weatherDescription = it[0].description
               }
                return@collect
            }

            createNotification(context, alarmId, weatherDescription)

            playAudio(context)
        }

    }
}