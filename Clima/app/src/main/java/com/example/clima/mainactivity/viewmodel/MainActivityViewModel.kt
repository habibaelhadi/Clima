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

    private val _languageFlow = MutableStateFlow(repository.getLanguage())
    val languageFlow = _languageFlow

    private val _splashFlow = MutableStateFlow(repository.getSplashState())
    val splashFlow = _splashFlow

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

    private fun getLanguage(){
        viewModelScope.launch {
            _languageFlow.value = repository.getLanguage()
        }
    }

    fun getLanguageForLocale(): String{
        getLanguage()
        return languageFlow.value
    }

    private fun getSplashState(){
        viewModelScope.launch {
            _splashFlow.value = repository.getSplashState()
        }
    }

    fun getSplashStateForActivity() : Boolean{
        getSplashState()
        return splashFlow.value
    }

    fun setSplashState(state : Boolean){
        viewModelScope.launch {
            repository.setSplashState(state)
        }
    }

    fun  getLocation() : String{
        viewModelScope.launch {
            _locationFlow.value = repository.getLocationSource()
        }
        return locationFlow.value
    }

    fun getLocationChangeFlow(){
        viewModelScope.launch {
            repository.getLocationChange().collect{
                repository.setMapCoordinates(it.first,it.second)
            }
        }
    }

    fun setMapCoordinate(lat : String,lng : String){
        viewModelScope.launch {
            repository.setMapCoordinates(lat,lng)
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