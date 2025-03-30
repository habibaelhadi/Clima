package com.example.clima.composable.alarms.manager

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.clima.composable.alarms.broadcastrecievers.AlarmBroadcastCancelReceiver
import com.example.clima.composable.alarms.broadcastrecievers.AlarmBroadcastReceiver
import com.example.clima.model.Alarm
import com.example.clima.utilites.parseTimeToMillis

class AlarmScheduler(val context: Context) : IAlarmScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    override fun scheduleAlarm(alarm: Alarm) {
        // Schedule main alarm
        val alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
            putExtra("ALARM_ID", alarm.id)
        }

        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule cancellation alarm (using negative ID to differentiate)
        val cancelIntent = Intent(context, AlarmBroadcastCancelReceiver::class.java).apply {
            putExtra("ALARM_ID", alarm.id)
        }

        val cancelPendingIntent = PendingIntent.getBroadcast(
            context,
            -alarm.id, // Negative ID for cancellation
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmTime = parseTimeToMillis(alarm.startTime)
        val endTime = parseTimeToMillis(alarm.endTime)
        val cancelTime = alarmTime + (endTime * 1000L)

        // Set both alarms
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                alarmPendingIntent
            )
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                cancelTime,
                cancelPendingIntent
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                alarmPendingIntent
            )
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                cancelTime,
                cancelPendingIntent
            )
        }
    }

    override fun cancelAlarm(alarm: Alarm) {
        val alarmIntent = Intent(context, AlarmBroadcastReceiver::class.java)
        val cancelIntent = Intent(context, AlarmBroadcastCancelReceiver::class.java)

        val alarmPendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val cancelPendingIntent = PendingIntent.getBroadcast(
            context,
            -alarm.id,
            cancelIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(alarmPendingIntent)
        alarmManager.cancel(cancelPendingIntent)

    }

}