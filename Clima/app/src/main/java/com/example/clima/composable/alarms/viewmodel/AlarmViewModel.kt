package com.example.clima.composable.alarms.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clima.model.Alarm
import com.example.clima.repo.WeatherRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AlarmViewModel(val repo : WeatherRepo) : ViewModel()  {

    private val _alarms = MutableStateFlow<List<Alarm>>(emptyList())
    val alarms = _alarms.asStateFlow()

    fun getAlarms(){
        viewModelScope.launch {
            repo.getAlarms()
                .collect{
                _alarms.value = it
            }
        }
    }

   fun insertAlarm(alarm : Alarm){
        viewModelScope.launch (Dispatchers.IO){
            repo.insertAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: Alarm){
        viewModelScope.launch {
            repo.deleteAlarm(alarm)
        }
    }

    class AlarmFactory(private val repo: WeatherRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AlarmViewModel(repo) as T
        }
    }
}