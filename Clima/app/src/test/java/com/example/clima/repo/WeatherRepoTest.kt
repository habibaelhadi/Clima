package com.example.clima.repo

import com.example.clima.local.IWeatherLocalDataSource
import com.example.clima.local.SharedPreferencesDataSource
import com.example.clima.model.Alarm
import com.example.clima.model.CurrentWeather
import com.example.clima.model.HomePOJO
import com.example.clima.remote.IWeatherRemoteDataSource
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

class WeatherRepoTest{

    private lateinit var repo: WeatherRepo
    private val remoteDataSource: IWeatherRemoteDataSource = mockk()
    private val localDataSource: IWeatherLocalDataSource = mockk()
    private val sharedPreferencesDataSource: SharedPreferencesDataSource = mockk()

    @Before
    fun setUp() {
        repo = WeatherRepo.getInstance(remoteDataSource,
            localDataSource,
            sharedPreferencesDataSource)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCurrentWeather_returnData() = runTest {

        // Given
        val latitude = 30.0
        val longitude = 31.0
        val units = "metric"
        val lang = "en"

        val expectedWeather = mockk<CurrentWeather>()

        coEvery { remoteDataSource.getCurrentWeather(latitude, longitude, units, lang) } returns flowOf(expectedWeather)

        // When
        val result = repo.getCurrentWeather(latitude, longitude, units, lang)
        advanceUntilIdle()

        // Then
        result.collect { weather ->
            assertEquals(expectedWeather, weather)
        }

        coVerify { remoteDataSource.getCurrentWeather(latitude, longitude, units, lang) }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getCachedHome_returnCachedHome() = runTest {

        // Given
        val expectedHomeData = mockk<HomePOJO>()

        every { localDataSource.getCachedHome() } returns flowOf(expectedHomeData)

        // When
        val result = repo.getCachedHome()
        advanceUntilIdle()

        // Then
        result.collect { homeData ->
            assertEquals(expectedHomeData, homeData)
        }

        verify { localDataSource.getCachedHome() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertAlarm_callLocalDataSourceInsertAlarm() = runTest {
        // Given
        val alarm = mockk<Alarm>()

        coEvery { localDataSource.insertAlarm(alarm) } just Runs

        // When
        repo.insertAlarm(alarm)
        advanceUntilIdle()

        // Then
        coVerify { localDataSource.insertAlarm(alarm) }
    }
}