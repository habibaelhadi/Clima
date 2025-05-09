package com.example.clima.composable.favourites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clima.model.FavouritePOJO
import com.example.clima.repo.WeatherRepo
import com.example.clima.utilites.Response
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavouriteViewModel(val repository: WeatherRepo) : ViewModel() {
    private val _favouriteLocations =
        MutableStateFlow<Response<List<FavouritePOJO>>>(Response.Loading)
    val favouriteLocations = _favouriteLocations.asStateFlow()

    private val mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    fun getFavouriteCities() {
        viewModelScope.launch {
            try {
                repository.getWeather()
                    .catch {
                        _favouriteLocations.value = Response.Failure(it.message.toString())
                    }
                    .map { it.sortedBy { it.country } }
                    .collect {
                        _favouriteLocations.value = Response.Success(it)
                    }
            } catch (e: Exception) {
                _favouriteLocations.value = Response.Failure(e.message.toString())
            }
        }
    }

    fun insertFavouriteCity(city:FavouritePOJO){
        viewModelScope.launch {
            try{
                repository.insertWeather(city)
            }catch(e:Exception){
                mutableMessage.emit("Couldn't Insert Location To Favourites ")
            }
        }
    }
    fun deleteFavouriteCity(city: FavouritePOJO) {
        viewModelScope.launch {
            try{
                repository.deleteWeather(city)
                mutableMessage.emit("Location Deleted Successfully!")
            }catch (e : Exception){
                mutableMessage.emit("Couldn't Delete Location From Favourites ")
            }
        }
    }

    class FavFactory(private val repo: WeatherRepo) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FavouriteViewModel(repo) as T
        }
    }
}