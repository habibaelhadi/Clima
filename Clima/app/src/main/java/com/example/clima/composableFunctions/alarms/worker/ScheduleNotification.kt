package com.example.clima.composableFunctions.alarms.worker

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.clima.model.FavouritePOJO
import com.google.gson.Gson
import java.util.concurrent.TimeUnit

fun scheduleNotification(context: Context, location: FavouritePOJO, delayInMillis: Long) {
    val workRequest = OneTimeWorkRequestBuilder<AlarmWorker>()
        .setInitialDelay(delayInMillis, TimeUnit.MILLISECONDS)
        .setInputData(workDataOf("location" to Gson().toJson(location))) // Pass data
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}
