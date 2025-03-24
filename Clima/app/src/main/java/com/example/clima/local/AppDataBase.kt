package com.example.clima.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.clima.model.DataBaseTable

@Database(entities = [DataBaseTable::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object{
        const val DATABASE_NAME = "Weather data"

        @Volatile
        private var INSTANCE : AppDataBase ? = null

        fun getInstance(context: Context) : AppDataBase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}