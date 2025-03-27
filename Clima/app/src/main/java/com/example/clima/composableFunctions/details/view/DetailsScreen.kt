package com.example.clima.composableFunctions.details.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clima.composableFunctions.details.viewmodel.DetailsViewModel
import com.example.clima.composableFunctions.home.view.AirQuality
import com.example.clima.composableFunctions.home.view.CurrentWeather
import com.example.clima.composableFunctions.home.view.HourlyWeather
import com.example.clima.composableFunctions.home.view.LocationDetails
import com.example.clima.composableFunctions.home.view.WeeklyForecast
import com.example.clima.local.AppDataBase
import com.example.clima.local.WeatherLocalDataSource
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import com.example.clima.utilites.Response

@Composable
fun DetailsScreen(
    showFAB: MutableState<Boolean>,
    latitude: Number, longitude: Number
) {
    showFAB.value = false
    val detailsFactory =
        DetailsViewModel.DetailsFactory(
            WeatherRepo.getInstance(
                WeatherRemoteDataSource(RetrofitProduct.retrofit),
                WeatherLocalDataSource(AppDataBase.getInstance(LocalContext.current).weatherDao())
            )
        )
    val viewModel: DetailsViewModel = viewModel(factory = detailsFactory)
    viewModel.getWeather(latitude.toDouble(), longitude.toDouble())
    val uiState by viewModel.currentWeather.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is Response.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                ) { CircularProgressIndicator() }
            }

            is Response.Failure -> {
                val error = (uiState as Response.Failure).error
                Log.e("TAG", "HomeScreen: $error")
            }

            is Response.Success -> {
                val (currentWeather, forecast, hourly) = (uiState as Response.Success).data
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    LocationDetails(
                        modifier = Modifier.padding(top = 10.dp),
                        location = currentWeather.name
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CurrentWeather(
                        weatherResponse = currentWeather
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    AirQuality(
                        airQuality = currentWeather
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    WeeklyForecast(
                        data = forecast
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HourlyWeather(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp),
                        data = hourly
                    )
                }
            }
        }
    }
}