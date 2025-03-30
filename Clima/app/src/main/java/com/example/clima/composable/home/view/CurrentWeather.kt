package com.example.clima.composable.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.clima.R
import com.example.clima.model.CurrentWeather
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.White
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.ui.theme.colorGradient2
import com.example.clima.ui.theme.colorGradient3
import com.example.clima.utilites.displayDate
import com.example.clima.utilites.formatNumberBasedOnLanguage

@Composable
fun CurrentWeather(
    modifier: Modifier = Modifier,
    weatherResponse : CurrentWeather
) {
    val tempActual = weatherResponse.main.temp.toInt().toString()
    val temp = formatNumberBasedOnLanguage(tempActual)

    val feelsLikeActual = weatherResponse.main.feels_like.toInt().toString()
    val feelsLike = formatNumberBasedOnLanguage(feelsLikeActual)

    val foreCast = weatherResponse.weather[0].description
    val displayDate = displayDate(weatherResponse.dt.toLong(), "EEEE, dd MMMM")
    val date = formatNumberBasedOnLanguage(displayDate)
    val lottieCondition = detectLottie(weatherResponse.weather[0].icon)
    val iconCondition = detectIcon(weatherResponse.weather[0].icon)

    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (foreCastImage, foreCastValue, title, description, background) = createRefs()

        CardBackground(modifier = Modifier.constrainAs(background) {
            linkTo(
                start = parent.start,
                end = parent.end,
                top = parent.top,
                bottom = description.bottom,
                topMargin = 24.dp
            )
            height = Dimension.fillToConstraints
        },
            iconCode = lottieCondition)

        Image(painter = painterResource(id = iconCondition),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .height(175.dp)
                .constrainAs(foreCastImage) {
                    start.linkTo(anchor = parent.start, margin = 4.dp)
                    top.linkTo(anchor = parent.top)
                })

        Text(text = foreCast,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontSize = 20.sp,
            color = Gray,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.constrainAs(title) {
                start.linkTo(anchor = parent.start, margin = 24.dp)
                top.linkTo(anchor = foreCastImage.bottom)
            })

        Text(text = date,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontSize = 16.sp,
            color = Gray,
            modifier = Modifier
                .constrainAs(description) {
                    start.linkTo(anchor = title.start)
                    top.linkTo(anchor = title.bottom)
                }
                .padding(bottom = 24.dp))

        ForeCastValue(modifier = Modifier.constrainAs(foreCastValue) {
            end.linkTo(anchor = parent.end, margin = 24.dp)
            top.linkTo(anchor = foreCastImage.top)
            bottom.linkTo(anchor = foreCastImage.bottom)
        },
            degree = temp,
            description = feelsLike)
    }


}

@Composable
private fun CardBackground(
    modifier: Modifier = Modifier,
    iconCode: Int
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.linearGradient(
                    0f to colorGradient1, 0.25f to colorGradient2, 1f to colorGradient3
                ), shape = RoundedCornerShape(32.dp)
            )
    ) {

        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(iconCode))
        val progress by animateLottieCompositionAsState(
            composition = composition, iterations = LottieConstants.IterateForever
        )

        LottieAnimation(
            composition = composition, progress = { progress }, modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ForeCastValue(
    modifier: Modifier = Modifier,
    degree: String ,
    description: String
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.Start
    ) {
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            Text(
                text = degree, fontFamily = FontFamily(Font(R.font.exo2)), style = TextStyle(
                    brush = Brush.linearGradient(
                        0f to White, 1f to White.copy(alpha = 0.3f)
                    ), fontSize = 80.sp, fontWeight = FontWeight.Black
                ), modifier = Modifier.padding(16.dp)
            )
            Text(
                text = stringResource(R.string.degree),
                fontFamily = FontFamily(Font(R.font.exo2)),
                style = TextStyle(
                    brush = Brush.linearGradient(
                        0f to White, 1f to White.copy(alpha = 0.3f)
                    ), fontSize = 70.sp, fontWeight = FontWeight.Light
                )
            )
        }
        Text(
            text = stringResource(R.string.feels_like, description),
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontSize = 16.sp,
            color = Gray,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

fun detectLottie(iconCode : String) : Int{
    return when (iconCode) {
        "02d" -> R.raw.cloudy
        "03d" -> R.raw.cloudy
        "04d" -> R.raw.cloudy
        "09d" -> R.raw.rain_lottie
        "10d" -> R.raw.rain_lottie
        "11d" -> R.raw.thunder
        "13d" -> R.raw.snow
        "02nd" -> R.raw.cloudy
        "03n" -> R.raw.cloudy
        "04n" -> R.raw.cloudy
        "09n" -> R.raw.rain_lottie
        "10n" -> R.raw.rain_lottie
        "11n" -> R.raw.thunder
        "13n" -> R.raw.snow
        else -> R.raw.cloudy
    }
}

fun detectIcon(iconCode : String) : Int{
     return when (iconCode) {
         "01d" -> R.drawable.clear_sky_day
         "02d" -> R.drawable.few_clouds_day
         "03d" -> R.drawable.scattered_clouds
         "04d" -> R.drawable.broken_clouds
         "09d" -> R.drawable.shower_rain
         "10d" -> R.drawable.rain_day
         "11d" -> R.drawable.thunderstorm
         "13d" -> R.drawable.snow
         "50d" -> R.drawable.mist
         "01n" -> R.drawable.clear_sky_night
         "02nd" -> R.drawable.few_clouds_night
         "03n" -> R.drawable.scattered_clouds
         "04n" -> R.drawable.broken_clouds
         "09n" -> R.drawable.shower_rain
         "10n" -> R.drawable.rain_night
         "11n" -> R.drawable.thunderstorm
         "13n" -> R.drawable.snow
         "50n" -> R.drawable.mist
         else -> R.drawable.few_clouds_day
     }
}
