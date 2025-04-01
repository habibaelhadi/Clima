package com.example.clima.composable.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clima.model.CurrentWeather
import com.example.clima.model.ForeCast
import com.example.clima.model.HomePOJO
import com.example.clima.repo.WeatherRepo
import com.example.clima.utilites.Response
import com.example.clima.utilites.dailyForecasts
import com.example.clima.utilites.getLanguageCode
import com.example.clima.utilites.getUnitCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeViewModel(private val weatherRepo: WeatherRepo) : ViewModel() {
    private val _currentWeather =
        MutableStateFlow<Response<Triple<CurrentWeather,
                List<ForeCast.ForecastWeather>,List<ForeCast.ForecastWeather>>>> (Response.Loading)
    val currentWeather = _currentWeather.asStateFlow()

    private val _cachedHome = MutableStateFlow<Response<HomePOJO>>(Response.Loading)
    val cachedHome = _cachedHome.asStateFlow()

    private val _tempUnit = MutableStateFlow(weatherRepo.getTemperatureUnit())
    val tempUnit = _tempUnit.asStateFlow()

    private val _windUnit = MutableStateFlow(weatherRepo.getWindSpeedUnit())
    val windUnit = _windUnit.asStateFlow()

    private val _language = MutableStateFlow(weatherRepo.getLanguage())
    val language = _language.asStateFlow()

    private val _location = MutableStateFlow(weatherRepo.getMapCoordinates())
    val location = _location.asStateFlow()

    init {
        getLocation()
        getLocationChangeFlow()
    }

    fun getWeather() {
        viewModelScope.launch(Dispatchers.IO) {

            val lat = weatherRepo.getMapCoordinates().first.toDouble()
            val lng = weatherRepo.getMapCoordinates().second.toDouble()
            val temp = getUnitCode(weatherRepo.getTemperatureUnit())
            val savedLanguage = getLanguageCode(weatherRepo.getLanguage()).toString()

            try {
                val current = async {
                    weatherRepo.getCurrentWeather(lat, lng, temp, savedLanguage)
                        .catch {
                            _currentWeather.value = Response.Failure(it.message.toString())
                        }
                        .first()
                }
                val forecast = async {
                    weatherRepo.getForecast(lat, lng, temp, savedLanguage)
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
                    weatherRepo.getForecast(lat, lng, temp, savedLanguage)
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

    fun insertCacheHome(currentWeather: CurrentWeather,
                                 foreCast: List<ForeCast.ForecastWeather>,
                                hours : List<ForeCast.ForecastWeather>){
        val home = HomePOJO(
            currentWeather = currentWeather,
            foreCast = foreCast,
            hours = hours
        )
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepo.insertCacheHome(home)
        }
    }

    fun getCachedHome(){
        viewModelScope.launch{
            weatherRepo.getCachedHome()
                .collect{
                    _cachedHome.value = Response.Success(it)
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

    private fun getLocation(){
        viewModelScope.launch {
            _location.value = weatherRepo.getMapCoordinates()
        }
    }

    private fun getLocationChangeFlow(){
        viewModelScope.launch {
            weatherRepo.getLocationChange().collect{
                _location.value = it
            }
        }
    }

    class HomeFactory(private val repo: WeatherRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return HomeViewModel(repo) as T
        }
    }
}