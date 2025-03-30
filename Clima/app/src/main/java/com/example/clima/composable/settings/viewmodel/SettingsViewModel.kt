package com.example.clima.composable.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.clima.repo.WeatherRepo
import com.google.android.libraries.places.api.net.PlacesClient

class SettingsViewModel (private val placesClient: PlacesClient,
                         private val repository: WeatherRepo
): ViewModel(){

    class SettingsViewModelFactory(private val placesClient: PlacesClient,
                              private val repository: WeatherRepo) : ViewModelProvider.Factory
    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SettingsViewModel(placesClient,repository) as T
        }
    }
}