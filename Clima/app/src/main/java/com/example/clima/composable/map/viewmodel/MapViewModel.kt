package com.example.clima.composable.map.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clima.model.FavouritePOJO
import com.example.clima.repo.WeatherRepo
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapViewModel(private val placesClient: PlacesClient,
                         private val repository: WeatherRepo): ViewModel() {

    class MapViewModelFactory(private val placesClient: PlacesClient,
                                    private val repository: WeatherRepo) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MapViewModel(placesClient,repository) as T
        }
    }

    private val mutableSearchText = MutableStateFlow("")
    val searchText: StateFlow<String> = mutableSearchText

    private val mutablePredictions = MutableStateFlow<List<AutocompletePrediction>>(emptyList())
    val predictions: StateFlow<List<AutocompletePrediction>> = mutablePredictions

    private val mutableSelectedLocation = MutableStateFlow<Location?>(null)
    val selectedLocation: StateFlow<Location?> get() = mutableSelectedLocation

    private val mutableMessage = MutableSharedFlow<String>()
    val message = mutableMessage.asSharedFlow()

    fun onSearchQueryChanged(query: String) {
        mutableSearchText.value = query
        fetchPredictions(query)
    }

    fun onPlaceSelected(placeId: String) {
        fetchPlaceDetails(placeId)
        mutableSearchText.value = ""
        mutablePredictions.value = emptyList()
    }

    private fun fetchPredictions(query: String) {
        if (query.isEmpty()) {
            mutablePredictions.value = emptyList()
            return
        }

        viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    mutablePredictions.value = response.autocompletePredictions
                }
                .addOnFailureListener { exception ->
                    Log.e("MapViewModel", "Error fetching predictions: ${exception.message}")
                }
        }
    }

    fun fetchPlaceDetails(placeId: String) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()
        placesClient.fetchPlace(request)
            .addOnSuccessListener { response ->
                response.place.latLng?.let { latLng ->
                    mutableSelectedLocation.value = Location("").apply {
                        latitude = latLng.latitude
                        longitude = latLng.longitude
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("MapViewModel", "Error fetching place details: ${exception.message}")
            }
    }

    fun updateSelectedLocation(latLng: LatLng) {
        mutableSelectedLocation.value = Location("").apply {
            latitude = latLng.latitude
            longitude = latLng.longitude
        }
    }

    fun insertFavouriteLocation(lat: Double, lon: Double, country: String, city: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val currentWeather = repository.getCurrentWeather(lat,lon,"metric","en").first()
                val forecast = repository.getForecast(lat,lon,"metric","en").first()
                val favouriteLocation = FavouritePOJO(latitude = lat, longitude = lon,currentWeather,forecast,
                    country,city)
                repository.insertWeather(favouriteLocation)
                mutableMessage.emit("Location Added Successfully!")
            } catch (ex: Exception){
                mutableMessage.emit("Couldn't Add Location To Favourites ${ex.message}")
            }
        }
    }

    fun setMapCoordinates(lat : String,lng : String){
        viewModelScope.launch {
            repository.setMapCoordinates(lat,lng)
            mutableMessage.emit("Location Added Successfully!")
        }
    }
}