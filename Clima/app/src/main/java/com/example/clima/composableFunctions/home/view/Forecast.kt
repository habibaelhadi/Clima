package com.example.clima.composableFunctions.home.view

import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clima.R
import com.example.clima.model.ForeCast
import com.example.clima.ui.theme.Black
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.White
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.ui.theme.colorGradient2
import com.example.clima.ui.theme.colorGradient3
import com.example.clima.utilites.ForecastItem
import com.example.clima.utilites.convertAPIResponse
import com.example.clima.utilites.fromHex

@Composable
fun WeeklyForecast(
    modifier: Modifier = Modifier,
    data: List<ForeCast.ForecastWeather>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ForecastHeader()

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = data
            ) { item ->
                ForecastCard(item = item.convertAPIResponse(
                    forecast = item,
                    true))
            }
        }
    }
}

@Composable
private fun ForecastHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.weekly_forecast),
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontSize = 20.sp,
            color = Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ForecastCard(
    modifier: Modifier = Modifier,
    item: ForecastItem
) {
    val updatedModifier = remember (item.isSelected){
        if(item.isSelected) {
            modifier.background(
                shape = RoundedCornerShape(percent = 50),
                brush = Brush.verticalGradient(
                    0f to colorGradient3,
                    0.5f to colorGradient2,
                    1f to colorGradient1
                )
            )
        }else{
            modifier
        }
    }

    val primaryTextColor = remember(item.isSelected){
        if(item.isSelected) Gray else Black
    }

    val tempTextStyle = remember (item.isSelected){
        if (item.isSelected){
            TextStyle(
                brush = Brush.verticalGradient(
                    0f to White,
                    1f to White.copy(alpha = 0.3f)
                ),
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }else{
            TextStyle(
                color = Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )
        }
    }

    Column(
        modifier = updatedModifier
            .width(65.dp)
            .padding(
                horizontal = 10.dp,
                vertical = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item.dayOfWeek,
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily(Font(R.font.exo2)),
            color = primaryTextColor,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = item.date,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily(Font(R.font.exo2)),
            color = primaryTextColor,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(8.dp))
        WeatherIcon(icon = item.icon)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.temp+stringResource(R.string.degree),
            style = tempTextStyle
        )
        Spacer(modifier = Modifier.height(8.dp))
        AirQualityIndicator(
            value = item.airQuality,
            color = item.airQualityIndicatorColor
        )
    }
}

@Composable
private fun WeatherIcon(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxSize()
                .size(24.dp)
        )
    }
}

@Composable
private fun AirQualityIndicator(
    modifier: Modifier = Modifier,
    color: String,
    value: String
){
    Surface (
        modifier = modifier,
        color = Color.fromHex(color),
        contentColor = Black,
        shape = RoundedCornerShape(8.dp)
    ){
        Box(
            modifier = Modifier
                .padding(vertical = 2.dp)
                .width(55.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = value+stringResource(R.string.degree)
            )
        }
    }
}