package com.example.clima.mainactivity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clima.repo.WeatherRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel (private val repository: WeatherRepo): ViewModel() {

    private val _locationFlow = MutableStateFlow(repository.getLocationSource())
    val locationFlow = _locationFlow

    fun getLocationSourceFlow(gps : String,onGpsSelected: () -> Unit) {
        viewModelScope.launch {
            repository.getLocationSourceFlow().collect { source ->
                repository.setLocationSource(source)
                if (source == gps) {
                    onGpsSelected()
                }
            }
        }
    }

    fun getLocationChangeFlow(){
        viewModelScope.launch {
            repository.getLocationChange().collect{
                repository.setMapCoordinates(it.first,it.second)
            }
        }
    }

    class MainActivityViewModelFactory(
        private val repository: WeatherRepo) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainActivityViewModel(repository) as T
        }
    }
}