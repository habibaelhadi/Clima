package com.example.clima.composableFunctions.alarms.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.clima.composableFunctions.alarms.view.showNotification
import com.example.clima.model.FavouritePOJO
import com.google.gson.Gson

class AlarmWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val locationJson = inputData.getString("location") ?: return Result.failure()
        val location = Gson().fromJson(locationJson, FavouritePOJO::class.java)

        showNotification(applicationContext, location)

        return Result.success()
    }
}