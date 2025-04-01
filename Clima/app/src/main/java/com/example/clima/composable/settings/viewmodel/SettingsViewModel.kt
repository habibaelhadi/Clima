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

   init {
       getLocationSourceFlow()
   }

    fun setLocationSource(location : String){
        viewModelScope.launch {
            repository.setLocationSource(location)
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