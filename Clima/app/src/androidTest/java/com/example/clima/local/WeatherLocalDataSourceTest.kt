package com.example.clima.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.clima.model.Alarm
import com.example.clima.model.FavouritePOJO
import com.example.clima.model.HomePOJO
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceTest{

    private lateinit var localDataSource: WeatherLocalDataSource
    private val weatherDao: WeatherDao = mockk(relaxed = true)

    @Before
    fun setUp() {
        localDataSource = WeatherLocalDataSource(weatherDao)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertCacheHome_callWeatherDaoInsertCacheHome() = runTest {
        // Given
        val homeData = mockk<HomePOJO>()

        coEvery { weatherDao.insertCacheHome(homeData) } just Runs

        // When
        localDataSource.insertCacheHome(homeData)
        advanceUntilIdle()

        // Then
        coVerify { weatherDao.insertCacheHome(homeData) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAlarm_callWeatherDaoGetAlarm() = runTest {
        // Given
        val alarmId = 1
        val expectedAlarm = mockk<Alarm>()

        coEvery { weatherDao.getAlarm(alarmId) } returns expectedAlarm

        // When
        val result = localDataSource.getAlarm(alarmId)
        advanceUntilIdle()

        // Then
        assertEquals(expectedAlarm, result)
        coVerify { weatherDao.getAlarm(alarmId) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteWeather_callWeatherDaoDeleteWeather() = runTest {
        // Given
        val weather = mockk<FavouritePOJO>()

        coEvery { weatherDao.deleteWeather(weather) } just Runs

        // When
        localDataSource.deleteWeather(weather)
        advanceUntilIdle()

        // Then
        coVerify { weatherDao.deleteWeather(weather) }
    }
}