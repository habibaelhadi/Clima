package com.example.clima.composableFunctions.alarms.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clima.R
import com.example.clima.ui.theme.colorGradient1

@Composable
fun AlarmsScreen() {

    var isSheetOpen = remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        isSheetOpen.value = true
                    },
                    containerColor = colorGradient1,
                    shape = CircleShape
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Favorite",
                        tint = Color.White
                    )
                }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            NoAlarms()
            if (isSheetOpen.value) {
                AlarmsDetails(isSheetOpen)
            }
        }
    }


}

@Composable
fun NoAlarms() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_notifications_active_24),
                contentDescription = null,
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(colorGradient1),
                modifier = Modifier
                    .size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Alarms",
                fontFamily = FontFamily(Font(R.font.exo2)),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorGradient1,
                textAlign = TextAlign.Center
            )
        }

        Image(
            painter = painterResource(R.drawable.alert),
            contentDescription = null,
            modifier = Modifier
                .padding(horizontal = 48.dp)
                .size(600.dp)
        )
    }
}