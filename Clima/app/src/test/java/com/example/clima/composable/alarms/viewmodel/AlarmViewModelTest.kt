package com.example.clima.composable.alarms.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.clima.model.Alarm
import com.example.clima.repo.WeatherRepo
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AlarmViewModelTest{

    private lateinit var repo : WeatherRepo
    private lateinit var viewModel: AlarmViewModel

    @Before
    fun setup(){
        repo = mockk(relaxed = true)
        viewModel = AlarmViewModel(repo)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertAlarm_callRepoInsertAlarm() = runTest {

        // Given
        val alarm = mockk<Alarm>()

        // When
        viewModel.insertAlarm(alarm)
        advanceUntilIdle()

        // Then
        coVerify { repo.insertAlarm(alarm) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deleteAlarm_callRepoDeleteAlarm() = runTest {

        // Given
        val alarm = mockk<Alarm>()

        // When
        viewModel.deleteAlarm(alarm)
        advanceUntilIdle()

        // Then
        coVerify { repo.deleteAlarm(alarm) }
    }
}