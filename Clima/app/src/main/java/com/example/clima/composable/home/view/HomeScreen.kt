package com.example.clima.composable.home.view

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.clima.R
import com.example.clima.composable.home.viewmodel.HomeViewModel
import com.example.clima.local.AppDataBase
import com.example.clima.local.WeatherLocalDataSource
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import com.example.clima.ui.theme.Black
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.ui.theme.colorGradient2
import com.example.clima.ui.theme.colorGradient3
import com.example.clima.utilites.ConnectivityObserver
import com.example.clima.utilites.Response
import com.example.clima.utilites.checkForInternet
import com.example.clima.utilites.getAirItems
import com.example.clima.utilites.getLanguageCode


@Composable
fun HomeScreen() {
    val homeFactory =
        HomeViewModel.HomeFactory(
            WeatherRepo.getInstance(
                WeatherRemoteDataSource(RetrofitProduct.retrofit),
                WeatherLocalDataSource(AppDataBase.getInstance(LocalContext.current).weatherDao())
            )
        )
    val viewModel: HomeViewModel = viewModel(factory = homeFactory)
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    val savedLanguage = sharedPreferences.getString("app_language", "English") ?: "English"
    val language = getLanguageCode(savedLanguage).toString()
    val uiState by viewModel.currentWeather.collectAsStateWithLifecycle()

    val connectivityObserver = remember { ConnectivityObserver(context) }
    val isConnected by connectivityObserver.isConnected.collectAsStateWithLifecycle(
        initialValue = checkForInternet(
            context
        )
    )
    LaunchedEffect(isConnected, language) {
        if (isConnected) {
            viewModel.getWeather(language)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            !isConnected -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wind))
                    val progress by animateLottieCompositionAsState(
                        composition = composition, iterations = LottieConstants.IterateForever
                    )

                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            uiState is Response.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                ) { CircularProgressIndicator() }
            }

            uiState is Response.Failure -> {
                val error = (uiState as Response.Failure).error
                Log.e("TAG", "HomeScreen: $error")
            }

            uiState is Response.Success -> {
                val (currentWeather, forecast, hourly) = (uiState as Response.Success).data
                val data = getAirItems(context)
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
                        airQuality = currentWeather,
                        data = data
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
            text = stringResource(R.string.updating),
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.exo2)),
            color = Gray
        )
    }
}


