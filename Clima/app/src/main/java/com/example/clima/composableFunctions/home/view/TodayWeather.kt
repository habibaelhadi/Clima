package com.example.clima.composableFunctions.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clima.R
import com.example.clima.model.ForeCast
import com.example.clima.ui.theme.Black
import com.example.clima.utilites.ForecastItem
import com.example.clima.utilites.convertAPIResponse

@Composable
fun HourlyWeather(
    modifier: Modifier = Modifier,
    data: List<ForeCast.ForecastWeather>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HourlyHeader()

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(data) { hour ->
                HourlyCard(data = hour.convertAPIResponse(
                    forecast = hour)
                )
            }
        }
    }
}

@Composable
private fun HourlyHeader(
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.today_weather),
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontSize = 20.sp,
            color = Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HourlyCard(
    modifier: Modifier = Modifier,
    data: ForecastItem
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = data.hour,
            color = Black,
            fontSize = 14.sp
        )
        Image(
            painter = painterResource(data.icon),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 32.dp)
                .size(45.dp)
        )
        Text(
            text = data.desc,
            color = Black,
            fontSize = 14.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Text(
            text = data.temp+stringResource(R.string.degree),
            color = Black,
            fontSize = 14.sp,
            modifier = Modifier
                .padding(end = 16.dp)
        )
    }
}