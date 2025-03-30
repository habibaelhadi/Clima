package com.example.clima.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.clima.model.Alarm
import com.example.clima.model.FavouritePOJO
import com.example.clima.model.HomePOJO
import com.example.clima.utilites.Converter

@TypeConverters(Converter::class)
@Database(entities = [FavouritePOJO::class,Alarm::class, HomePOJO::class], version = 5,exportSchema = false)
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
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}