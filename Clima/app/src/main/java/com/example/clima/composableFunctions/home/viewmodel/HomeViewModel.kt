package com.example.clima.composableFunctions.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clima.model.CurrentWeather
import com.example.clima.repo.WeatherRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val weatherRepo: WeatherRepo) : ViewModel() {
    private val _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: MutableLiveData<CurrentWeather> = _currentWeather

    fun getCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO) {
           try{ val weather = weatherRepo.getCurrentWeather()
            _currentWeather.postValue(weather)}
           catch (e : Exception){
               Log.i("TAG", "getCurrentWeather: ${e.message}")
           }
        }

    }

    class HomeFactory(private val repo : WeatherRepo) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repo) as T
        }
    }
}