package com.example.clima.composable.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clima.repo.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel (private val repository: WeatherRepo): ViewModel(){

    private val _locationFlow = MutableStateFlow(repository.getLocationSource())
    val locationFlow = _locationFlow
    private val _languageFlow = MutableStateFlow(repository.getLanguage())
    val languageFlow = _languageFlow
    private val _tempFlow = MutableStateFlow(repository.getTemperatureUnit())
    val tempFlow = _tempFlow
    private val _windFlow = MutableStateFlow(repository.getWindSpeedUnit())
    val windFlow = _windFlow

   init {
       getLocationSourceFlow()
   }

    fun setLocationSource(location : String){
        viewModelScope.launch {
            repository.setLocationSource(location)
            _locationFlow.value = location
        }
    }

    fun setTemp(temp : String){
        viewModelScope.launch {
            repository.setTemperatureUnit(temp)
            _tempFlow.value = temp
        }
    }

    fun setWind(wind : String){
        viewModelScope.launch {
            repository.setWindSpeedUnit(wind)
            _windFlow.value = wind
        }
    }

    fun setLanguage(lang : String){
        viewModelScope.launch {
            repository.setLanguage(lang)
            languageFlow.value = lang
        }
    }

    private fun getLocationSourceFlow(){
        viewModelScope.launch {
            repository.getLocationSourceFlow()
                .collect{
                    _locationFlow.value = it
                }
        }
    }
    class SettingsViewModelFactory(
        private val repository: WeatherRepo) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(repository) as T
        }
    }
}