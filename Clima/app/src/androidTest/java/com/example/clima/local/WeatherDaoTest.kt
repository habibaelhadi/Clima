package com.example.clima.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.clima.model.Alarm
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherDaoTest {

    private lateinit var database : AppDataBase
    private lateinit var weatherDao: WeatherDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDataBase::class.java
        ).build()
        weatherDao = database.weatherDao()
    }

    @After
    fun closeDatabase(){
        database.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertAlarmsThenDeleteOne_returnsOneAlarm() = runTest {

        // Given
        weatherDao.insertAlarm(Alarm(1,"01:00","01:01"))
        weatherDao.insertAlarm(Alarm(3,"01:02","01:04"))
        weatherDao.deleteAlarm(Alarm(3,"01:02","01:04"))

        // When
        val result = weatherDao.getAlarms().first()
        advanceUntilIdle()

        // Then
        assertEquals(1, result.size)
        assertEquals("01:00", result.first().startTime)
        assertEquals("01:01", result.first().endTime)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertAlarm_ShouldInsertAlarmCorectly() = runTest {
        // Given a test alarm
        val alarm = Alarm(
            1,
            "08:00",
            "08:01"
        )
        //when
        weatherDao.insertAlarm(alarm)
        advanceUntilIdle()

        //then
        val alarms = weatherDao.getAlarms().first()
        assertEquals(1, alarms.size)
        assertEquals("08:00", alarms.first().startTime)
        assertEquals("08:01", alarms.first().endTime)
    }
}