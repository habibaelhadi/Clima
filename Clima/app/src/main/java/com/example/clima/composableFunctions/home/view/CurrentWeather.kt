package com.example.clima.composableFunctions.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.clima.R
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.White
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.ui.theme.colorGradient2
import com.example.clima.ui.theme.colorGradient3

@Composable
fun CurrentWeather(
    modifier: Modifier = Modifier,
    foreCast : String = "Rain showers",
    date : String = "Monday, 12 Feb"
) {
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val (foreCastImage, foreCastValue, title, description, background) = createRefs()

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
            color = Gray,
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
            color = Gray,
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
        Box(
            contentAlignment = Alignment.TopEnd
        ){
            Text(
                text = degree,
                fontFamily = FontFamily(Font(R.font.exo2)),
                style = TextStyle(
                    brush = Brush.linearGradient(
                        0f to White,
                        1f to White.copy(alpha = 0.3f)
                    ),
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Black
                ),
                modifier = Modifier.padding(16.dp)
            )
            Text(
                text = "°",
                fontFamily = FontFamily(Font(R.font.exo2)),
                style = TextStyle(
                    brush = Brush.linearGradient(
                        0f to White,
                        1f to White.copy(alpha = 0.3f)
                    ),
                    fontSize = 70.sp,
                    fontWeight = FontWeight.Light
                )
            )
        }
        Text(
            text = description,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontSize = 16.sp,
            color = Gray,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
