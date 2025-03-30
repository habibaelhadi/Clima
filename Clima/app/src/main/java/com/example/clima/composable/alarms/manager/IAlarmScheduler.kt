package com.example.clima.composable.alarms.manager

import com.example.clima.model.Alarm

interface IAlarmScheduler {
    fun scheduleAlarm(alarm: Alarm)
    fun cancelAlarm(alarm: Alarm)
}