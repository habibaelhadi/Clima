package com.example.clima.composableFunctions.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clima.model.CurrentWeather
import com.example.clima.repo.WeatherRepo
import com.example.clima.utilites.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val weatherRepo: WeatherRepo) : ViewModel() {
    private val _currentWeather = MutableStateFlow<Response<CurrentWeather>>(Response.Loading)
    val currentWeather = _currentWeather.asStateFlow()

    fun getCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO) {
           try{
               weatherRepo.getCurrentWeather(31.252321,29.992283,"metric","en")
                   .catch {
                       _currentWeather.value = Response.Failure(it.message.toString())
                   }
                   .collect {
                       _currentWeather.value = Response.Success(it)
                   }
           }
           catch (e : Exception){
               _currentWeather.value = Response.Failure(e.message.toString())
           }
        }
    }

    class HomeFactory(private val repo : WeatherRepo) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repo) as T
        }
    }
}