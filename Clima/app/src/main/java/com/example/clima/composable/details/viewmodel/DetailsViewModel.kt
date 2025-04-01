package com.example.clima.composable.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clima.model.CurrentWeather
import com.example.clima.model.ForeCast
import com.example.clima.repo.WeatherRepo
import com.example.clima.utilites.Response
import com.example.clima.utilites.dailyForecasts
import com.example.clima.utilites.getLanguageCode
import com.example.clima.utilites.getUnitCode
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class DetailsViewModel(private val weatherRepo: WeatherRepo) : ViewModel() {
    private val _currentWeather =
        MutableStateFlow<Response<Triple<CurrentWeather, List<ForeCast.ForecastWeather>,List<ForeCast.ForecastWeather>>>>(Response.Loading)
    val currentWeather = _currentWeather.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _currentWeather.value = Response.Failure(throwable.message.toString())
    }

    private val _tempUnit = MutableStateFlow(weatherRepo.getTemperatureUnit())
    val tempUnit = _tempUnit.asStateFlow()

    private val _windUnit = MutableStateFlow(weatherRepo.getWindSpeedUnit())
    val windUnit = _windUnit.asStateFlow()

    private val _language = MutableStateFlow(weatherRepo.getLanguage())
    val language = _language.asStateFlow()

    fun getWeather(lat: Double, lng: Double) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val temp = getUnitCode(weatherRepo.getTemperatureUnit())
            val language = getLanguageCode(weatherRepo.getLanguage()).toString()
            try {
                val current = async {
                    weatherRepo.getCurrentWeather(lat, lng, temp, language)
                        .catch {
                            _currentWeather.value = Response.Failure(it.message.toString())
                        }
                        .first()
                }
                val forecast = async {
                    weatherRepo.getForecast(lat, lng, temp, language)
                        .catch {
                            _currentWeather.value = Response.Failure(it.message.toString())
                        }
                        .map { forecastMap ->
                            forecastMap.dailyForecasts()
                                .flatMap{ it.value.take(1) }
                        }
                        .firstOrNull() ?: emptyList()
                }
                val hourly = async {
                    weatherRepo.getForecast(lat, lng, temp, language)
                        .catch {
                            _currentWeather.value = Response.Failure(it.message.toString())
                        }
                        .map { forecastMap ->
                            forecastMap.list.take(8)
                        }
                        .first()
                }
                val data = Triple(current.await(), forecast.await(),hourly.await())
                _currentWeather.value = Response.Success(data)
            } catch (e: Exception) {
                _currentWeather.value = Response.Failure(e.message.toString())
            }
        }
    }

    fun getTempUnit(){
        viewModelScope.launch {
            _tempUnit.value =  weatherRepo.getTemperatureUnit()
        }
    }

    fun getWindUnit(){
        viewModelScope.launch {
            _windUnit.value =  weatherRepo.getWindSpeedUnit()
        }
    }

    fun getLanguage(){
        viewModelScope.launch {
            _language.value = weatherRepo.getLanguage()
        }
    }

    class DetailsFactory(private val repo: WeatherRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return DetailsViewModel(repo) as T
        }
    }
}