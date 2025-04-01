package com.example.clima.composable.alarms.broadcastrecievers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.clima.mainactivity.view.MainActivity
import com.example.clima.composable.alarms.manager.AlarmScheduler
import com.example.clima.local.AppDataBase
import com.example.clima.local.SharedPreferencesDataSource
import com.example.clima.local.WeatherLocalDataSource
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import com.example.clima.utilites.MyMediaPlayer
import com.example.clima.utilites.createNotification
import com.example.clima.utilites.getLanguageCode
import com.example.clima.utilites.playAudio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        var alarmId = intent.getIntExtra("ALARM_ID", -1)
        Log.i("TAG", "onReceive: $alarmId")
        if (alarmId == -1) return

        val action = intent.getStringExtra("ALARM_ACTION")

        val repository =  WeatherRepo.getInstance(
            WeatherRemoteDataSource(RetrofitProduct.retrofit),
            WeatherLocalDataSource(AppDataBase.getInstance(context).weatherDao()),
            SharedPreferencesDataSource.getInstance(context)
        )

        CoroutineScope(Dispatchers.IO).launch {

            when(action){
                "START" -> {handleAlarmStart(context, alarmId, repository)}
                "STOP" -> {handleAlarmStop(context, intent, alarmId, repository)}
                "OPEN" -> {
                    handleAlarmStop(context, intent, alarmId, repository)
                    val mainIntent = Intent(context, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    context.startActivity(mainIntent)
                }
            }
        }
    }

    private suspend fun handleAlarmStop(context: Context, intent: Intent, alarmId: Int, repository: WeatherRepo) {
        val repository = WeatherRepo.getInstance(
            WeatherRemoteDataSource(RetrofitProduct.retrofit),
            WeatherLocalDataSource(AppDataBase.getInstance(context).weatherDao()),
            SharedPreferencesDataSource.getInstance(context)
        )
        val alarm = repository.getAlarm(alarmId)
        val isDeleteAction = intent.action == "DELETE" || intent.getBooleanExtra("isDismiss", false)

        alarm?.let {
            if (!isDeleteAction) {
                AlarmScheduler(context).cancelAlarm(it)
                repository.deleteAlarm(it)
            }

            MyMediaPlayer.stopAudio()
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(alarmId)

        }

    }

    private suspend fun handleAlarmStart(context: Context, alarmId: Int, repository: WeatherRepo) {
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