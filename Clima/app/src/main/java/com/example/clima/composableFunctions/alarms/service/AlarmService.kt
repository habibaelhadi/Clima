package com.example.clima.composableFunctions.alarms.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.clima.composableFunctions.alarms.worker.scheduleNotification
import com.example.clima.model.FavouritePOJO
import com.example.clima.utilites.calculateDelay
import com.google.gson.Gson

class AlarmService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val locationJson = intent?.getStringExtra("location") ?: return START_NOT_STICKY
        val location = Gson().fromJson(locationJson, FavouritePOJO::class.java)

        val delay = calculateDelay(3, 50)
        scheduleNotification(this, location, delay)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
