package com.example.clima.composableFunctions.home.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clima.R
import com.example.clima.composableFunctions.home.viewmodel.HomeViewModel
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import com.example.clima.ui.theme.Black
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.ui.theme.colorGradient2
import com.example.clima.ui.theme.colorGradient3
import com.example.clima.utilites.Response


@Preview
@Composable
fun HomeScreen() {
    val homeFactory =
        HomeViewModel.HomeFactory(WeatherRepo.getInstance(WeatherRemoteDataSource(RetrofitProduct.retrofit)))
    val viewModel: HomeViewModel = viewModel(factory = homeFactory)
    viewModel.getCurrentWeather()
    val uiState by viewModel.currentWeather.collectAsStateWithLifecycle()
    when(uiState){
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
            val currentWeather = (uiState as Response.Success).data
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 10.dp
                    )
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
            }
        }
    }
}

@Composable
fun LocationDetails(
    modifier: Modifier = Modifier,
    location: String
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_location_pin_24),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.height(10.dp)
            )
            Text(
                text = location,
                fontFamily = FontFamily(Font(R.font.exo2)),
                fontSize = 24.sp,
                color = Black,
                fontWeight = FontWeight.Bold
            )
        }
        ProgressBar()
    }
}

@Composable
private fun ProgressBar(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.linearGradient(
                    0f to colorGradient1,
                    0.25f to colorGradient2,
                    1f to colorGradient3
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 2.dp, horizontal = 10.dp)
    ) {
        Text(
            text = "Updating..",
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.exo2)),
            color = Gray
        )
    }
}


