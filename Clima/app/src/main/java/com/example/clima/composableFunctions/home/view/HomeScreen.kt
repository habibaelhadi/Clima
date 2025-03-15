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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clima.R
import com.example.clima.composableFunctions.home.viewmodel.HomeViewModel
import com.example.clima.model.CurrentWeather
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import java.util.Objects.hashCode


val colorGradient1 = Color(0xFF6A1B9A)
val colorGradient2 = Color(0xFF9C27B0)
val colorGradient3 = Color(0xFFCE93D8)

@Preview
@Composable
fun HomeScreen() {
    val homeFactory =
        HomeViewModel.HomeFactory(WeatherRepo.getInstance(WeatherRemoteDataSource(RetrofitProduct.retrofit)))
    val viewModel: HomeViewModel = viewModel(factory = homeFactory)
    val currentWeather = viewModel.currentWeather.observeAsState()
    viewModel.getCurrentWeather()
    Log.i("TAG", "HomeScreen: ${currentWeather.value}")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 24.dp,
                vertical = 10.dp
            )
    ) {
        LocationDetails(
            modifier = Modifier.padding(top = 10.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        CurrentWeather()
    }
}

@Composable
fun LocationDetails(
    modifier: Modifier = Modifier,
    location: String = "Alex"
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
                color = colorResource(R.color.black),
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
            color = colorResource(R.color.grey)
        )
    }
}


@Composable
fun CurrentWeather(
    modifier: Modifier = Modifier,
    foreCast : String = "Rain showers",
    date : String = "Monday, 12 Feb"
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (foreCastImage, foreCastValue, windImage, title, description, background) = createRefs()

        CardBackground(
            modifier = Modifier.constrainAs(background) {
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    top = parent.top,
                    bottom = description.bottom,
                    topMargin = 24.dp
                )
                height = Dimension.fillToConstraints
            }
        )

        Image(
            painter = painterResource(id = R.drawable.rain),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .height(175.dp)
                .constrainAs(foreCastImage) {
                    start.linkTo(anchor = parent.start, margin = 4.dp)
                    top.linkTo(anchor = parent.top)
                }
        )

        Text(
            text = foreCast,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontSize = 20.sp,
            color = colorResource(R.color.grey),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(anchor = parent.start,margin = 24.dp)
                top.linkTo(anchor = foreCastImage.bottom)
            }
        )

        Text(
            text = date,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontSize = 16.sp,
            color = colorResource(R.color.grey),
            modifier = Modifier.constrainAs(description) {
                start.linkTo(anchor = title.start)
                top.linkTo(anchor = title.bottom)
            }
                .padding(bottom = 24.dp)
        )

        ForeCastValue(
            modifier = Modifier.constrainAs(foreCastValue){
                end.linkTo(anchor = parent.end, margin = 24.dp)
                top.linkTo(anchor =foreCastImage.top)
                bottom.linkTo(anchor = foreCastImage.bottom)
            }
        )
    }
}

@Composable
private fun CardBackground(
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    0f to colorGradient1,
                    0.25f to colorGradient2,
                    1f to colorGradient3
                ),
                shape = RoundedCornerShape(32.dp)
            )
    )
}

@Composable
private fun ForeCastValue(
    modifier: Modifier = Modifier,
    degree : String = "21",
    description : String = "Feels like 26"
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ){
        Text(
            text = degree,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontSize = 64.sp,
            color = colorResource(R.color.white),
            fontWeight = FontWeight.Bold
        )
    }
}