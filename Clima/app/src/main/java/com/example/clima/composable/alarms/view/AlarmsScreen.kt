package com.example.clima.composable.alarms.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clima.R
import com.example.clima.composable.alarms.viewmodel.AlarmViewModel
import com.example.clima.local.AppDataBase
import com.example.clima.local.WeatherLocalDataSource
import com.example.clima.model.Alarm
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.ui.theme.colorGradient2
import com.example.clima.ui.theme.colorGradient3

@Composable
fun AlarmsScreen() {

    var isSheetOpen = remember { mutableStateOf(false) }

    val alarmFactory = AlarmViewModel.AlarmFactory(
        WeatherRepo.getInstance(
            WeatherRemoteDataSource(RetrofitProduct.retrofit),
            WeatherLocalDataSource(AppDataBase.getInstance(LocalContext.current).weatherDao())
        )
    )

    val viewModel: AlarmViewModel = viewModel(factory = alarmFactory)
    val alarms by viewModel.alarms.collectAsStateWithLifecycle()
    viewModel.getAlarms()

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
                        Icons.Default.DateRange,
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
            if(alarms.isEmpty()){
                NoAlarms()
            }else{
                AlarmsList(alarms)
            }
            if (isSheetOpen.value) {
                AlarmsDetails(isSheetOpen,viewModel)
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
                text = stringResource(R.string.alarms),
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

@Composable
fun AlarmsList(alarms: List<Alarm>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                text = stringResource(R.string.alarms),
                fontFamily = FontFamily(Font(R.font.exo2)),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorGradient1,
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 200.dp, max = 400.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(alarms.size) {
                AlarmCard(alarms[it])
            }
        }
    }
}

@Composable
fun AlarmCard(alarm: Alarm) {
    /*SwipeToDeleteContainer(
        item = data ,
        onDelete = { item ->
        },
        onRestore = {
        },
        snackbarHostState =
    ) {*/
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                ),
            shape = RoundedCornerShape(32.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(colorGradient3, colorGradient2, colorGradient1)
                        )
                    )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.baseline_notifications_active_24),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Row{
                        Text(
                            text = "Start date: ${alarm.startTime}",
                            fontFamily = FontFamily(Font(R.font.exo2)),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Gray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "End date: ${alarm.endTime}",
                            fontFamily = FontFamily(Font(R.font.exo2)),
                            fontSize = 18.sp,
                            color = Gray
                        )
                    }
                }
            }
        }
    }
//}