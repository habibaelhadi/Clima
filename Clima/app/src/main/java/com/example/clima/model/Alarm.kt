package com.example.clima.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm (
    @PrimaryKey
    val id : Int,
    val startTime : String,
    val endTime : String
)