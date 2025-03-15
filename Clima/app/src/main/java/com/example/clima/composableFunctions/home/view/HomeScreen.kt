package com.example.clima.composableFunctions.home.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clima.composableFunctions.home.viewmodel.HomeViewModel
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo

@Composable
fun HomeScreen(){
    val homeFactory = HomeViewModel.HomeFactory(WeatherRepo.getInstance(WeatherRemoteDataSource(RetrofitProduct.retrofit)))
    val viewModel : HomeViewModel = viewModel(factory = homeFactory)
    val currentWeather = viewModel.currentWeather.observeAsState()
    viewModel.getCurrentWeather()
    Log.i("TAG", "HomeScreen: ${currentWeather.value}")
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(text = "Home Screen")
    }
}