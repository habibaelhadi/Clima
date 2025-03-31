package com.example.clima.composable.details.view

import android.content.Context
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clima.composable.details.viewmodel.DetailsViewModel
import com.example.clima.composable.home.view.AirQuality
import com.example.clima.composable.home.view.CurrentWeather
import com.example.clima.composable.home.view.HourlyWeather
import com.example.clima.composable.home.view.LocationDetails
import com.example.clima.composable.home.view.WeeklyForecast
import com.example.clima.local.AppDataBase
import com.example.clima.local.WeatherLocalDataSource
import com.example.clima.model.FavouritePOJO
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import com.example.clima.utilites.Response
import com.example.clima.utilites.dailyForecasts
import com.example.clima.utilites.getAirItems
import com.example.clima.utilites.getLanguageCode
import com.example.clima.utilites.getTempUnit
import com.example.clima.utilites.getWindSpeedUnitSymbol

@Composable
fun DetailsScreen(
    location : FavouritePOJO
) {
    val detailsFactory =
        DetailsViewModel.DetailsFactory(
            WeatherRepo.getInstance(
                WeatherRemoteDataSource(RetrofitProduct.retrofit),
                WeatherLocalDataSource(AppDataBase.getInstance(LocalContext.current).weatherDao())
            )
        )
    val viewModel: DetailsViewModel = viewModel(factory = detailsFactory)
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val savedLanguage  by remember {
        mutableStateOf(sharedPreferences.getString("app_language",
            "English") ?: "English")
    }
    val savedTemp  by remember {
        mutableStateOf( sharedPreferences.getString("app_temp",
            "Celsius (°C)")?:"Celsius (°C)") }
    val savedWind  by remember {
        mutableStateOf(sharedPreferences.getString("app_wind",
            "Miles per hour (m/h)")?:"Miles per hour (m/h)") }

    val windUnit = context.getWindSpeedUnitSymbol(savedWind)
    val language = getLanguageCode(savedLanguage).toString()
    val temp = getTempUnit(savedTemp)

    LaunchedEffect (location){
        viewModel.getWeather(location.latitude, location.longitude,language,temp)
    }
    val uiState by viewModel.currentWeather.collectAsStateWithLifecycle()
    val data = getAirItems(context)

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
                Log.e("TAG", "DetailsScreen: FAILURE", )
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    LocationDetails(
                        modifier = Modifier.padding(top = 10.dp),
                        location = location.city
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    CurrentWeather(
                        weatherResponse = location.currentWeather,
                        tempUnit = temp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    AirQuality(
                        airQuality = location.currentWeather,
                        data = data,
                        windUnit = windUnit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    WeeklyForecast(
                        data = location.forecast.dailyForecasts()
                            .flatMap {
                                it.value.take(1)
                            },
                        tempUnit = temp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HourlyWeather(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp),
                        data = location.forecast.list.subList(0,8),
                        tempUnit = temp
                    )
                }
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
                        weatherResponse = currentWeather,
                        tempUnit = temp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    AirQuality(
                        airQuality = currentWeather,
                        data = data,
                        windUnit = windUnit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    WeeklyForecast(
                        data = forecast,
                        tempUnit = temp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HourlyWeather(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 200.dp),
                        data = hourly,
                        tempUnit = temp
                    )
                }
            }
        }
    }
}