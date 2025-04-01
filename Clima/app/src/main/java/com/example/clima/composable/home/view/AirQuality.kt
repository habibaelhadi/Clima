package com.example.clima.composable.home.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clima.R
import com.example.clima.model.CurrentWeather
import com.example.clima.ui.theme.Black
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.PurpleGrey40
import com.example.clima.utilites.AirQualityItem
import com.example.clima.utilites.formatNumberBasedOnLanguage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AirQuality(
    modifier: Modifier = Modifier,
    data: List<AirQualityItem>,
    airQuality : CurrentWeather,
    windUnit : String
) {
    val humidityActual = airQuality.main.humidity.toString()
    val humidity = formatNumberBasedOnLanguage(humidityActual)
    val pressureActual = airQuality.main.pressure.toString()
    val pressure = formatNumberBasedOnLanguage(pressureActual)
    val windSpeedActual = airQuality.wind.speed.toString()
    val windSpeed = formatNumberBasedOnLanguage(windSpeedActual)
    val cloudsActual = airQuality.clouds.all.toString()
    val clouds = formatNumberBasedOnLanguage(cloudsActual)

    data[0].value = windSpeed+windUnit
    data[1].value = pressure+ stringResource(R.string.hpa)
    data[2].value = humidity+ stringResource(R.string.percent)
    data[3].value = clouds+stringResource(R.string.percent)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = Gray

    ) {
        Column(
            modifier = Modifier.padding(
                vertical = 16.dp,
                horizontal = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AirQualityHeader()
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 2
            ) {
                data.onEach { item ->
                    AirQualityInfo(
                        data = item,
                        modifier = Modifier
                            .weight(weight = 1f)
                            .padding(vertical = 4.dp)
                    )
                }
            }
        }

    }
}

@Composable
private fun AirQualityHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.air_quality),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = stringResource(R.string.air_quality),
                color = Black,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.exo2))
            )
        }
    }
}

@Composable
private fun AirQualityInfo(
    modifier: Modifier = Modifier,
    data: AirQualityItem
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(data.icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Column(
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = data.title,
                color = PurpleGrey40,
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.exo2))
            )
            Text(
                text = data.value,
                color = Black,
                fontSize = 10.sp,
                fontFamily = FontFamily(Font(R.font.exo2))
            )
        }
    }
}