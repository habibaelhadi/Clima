package com.example.clima.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.clima.model.FavouritePOJO
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_table")
    fun getWeather(): Flow<List<FavouritePOJO>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: FavouritePOJO)

    @Delete
    suspend fun deleteWeather(weather: FavouritePOJO)
}