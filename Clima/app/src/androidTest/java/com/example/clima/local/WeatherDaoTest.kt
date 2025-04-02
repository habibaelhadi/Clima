package com.example.clima.local

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.clima.model.Alarm
import com.example.clima.model.FavouritePOJO
import com.example.clima.model.HomePOJO
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {

    private lateinit var weatherDao: WeatherDao

    @Before
    fun setUp() {
        weatherDao = mockk(relaxed = true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAlarms_returnAlarms() = runTest {

        // Given
        val expectedAlarms = listOf(mockk<Alarm>())

        every { weatherDao.getAlarms() } returns flowOf(expectedAlarms)

        // When
        val result = weatherDao.getAlarms()
        advanceUntilIdle()

        // Then
        result.collect { alarms ->
            assertEquals(expectedAlarms, alarms)
        }

        verify { weatherDao.getAlarms() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteWeather_callWeatherDaoDeleteWeather() = runTest {
        // Given
        val weather = mockk<FavouritePOJO>()

        coEvery { weatherDao.deleteWeather(weather) } just Runs

        // When
        weatherDao.deleteWeather(weather)
        advanceUntilIdle()

        // Then
        coVerify { weatherDao.deleteWeather(weather) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertCacheHome_callWeatherDaoInsertCacheHome() = runTest {
        // Given
        val homeData = mockk<HomePOJO>()

        coEvery { weatherDao.insertCacheHome(homeData) } just Runs

        // When
        weatherDao.insertCacheHome(homeData)
        advanceUntilIdle()

        // Then
        coVerify { weatherDao.insertCacheHome(homeData) }
    }
}