package com.example.clima.composable.alarms.broadcastrecievers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.clima.composable.alarms.manager.AlarmScheduler
import com.example.clima.composable.alarms.service.MyMediaPlayer
import com.example.clima.local.AppDataBase
import com.example.clima.local.WeatherLocalDataSource
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmBroadcastCancelReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        if (alarmId == -1) return

        val isDeleteAction = intent.action == "DELETE" || intent.getBooleanExtra("isDismiss", false)

        Log.i("CANCEL RECEIVER", "onReceive: $isDeleteAction")
        CoroutineScope(Dispatchers.IO).launch {
            val repository = WeatherRepo.getInstance(
                WeatherRemoteDataSource(RetrofitProduct.retrofit),
                WeatherLocalDataSource(AppDataBase.getInstance(context).weatherDao())
            )
            val alarm = repository.getAlarm(alarmId)

            alarm?.let {
                if (!isDeleteAction) {
                    AlarmScheduler(context).cancelAlarm(it)
                    repository.deleteAlarm(it)
                }

                MyMediaPlayer.stopAudio()
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(alarmId)

                Log.d("AlarmCancelReceiver", "Alarm ${it.id} cancelled")
            }
        }

    }
}