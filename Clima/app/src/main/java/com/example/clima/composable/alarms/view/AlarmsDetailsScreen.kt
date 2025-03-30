package com.example.clima.composable.alarms.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clima.R
import com.example.clima.composable.alarms.manager.AlarmScheduler
import com.example.clima.composable.alarms.viewmodel.AlarmViewModel
import com.example.clima.model.Alarm
import com.example.clima.ui.theme.Black
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.White
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.utilites.isValidTimeFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmsDetails(
    isSheetOpen: MutableState<Boolean>,
    viewModel: AlarmViewModel
) {
    var sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    if (isSheetOpen.value) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen.value = false },
            sheetState = sheetState,
            containerColor = Gray
        ) {
            BottomSheetContent(context, viewModel) { isSheetOpen.value = false }
        }
    }
}

@Composable
fun BottomSheetContent(context: Context, viewModel: AlarmViewModel, onDismiss: () -> Unit) {
    val date = stringResource(R.string.start_date)
    val timeStart = stringResource(R.string.start_time)
    val timeEnd = stringResource(R.string.end_time)
    val alarm = stringResource(R.string.alarm)

    val alarmScheduler = remember { AlarmScheduler(context) }

    var startDate by remember { mutableStateOf(date) }
    var startTime by remember { mutableStateOf(timeStart) }
    var endTime by remember { mutableStateOf(timeEnd) }
    var selectedOption by remember { mutableStateOf(alarm) }
    val options = listOf(alarm, stringResource(R.string.notification))
    val icons = listOf(
        R.drawable.clock,
        R.drawable.notification
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White, shape = RoundedCornerShape(16.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.set_an_alert),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateTimePickerField(context, "Date", startDate, R.drawable.calendar) {
            startDate = it
        }
        DateTimePickerField(context, "Start Time", startTime, R.drawable.timer) {
            startTime = it
        }
        DateTimePickerField(context, "End Time", endTime, R.drawable.timer) {
            endTime = it
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.notify_me_by),
            fontWeight = FontWeight.SemiBold,
            color = Black
        )

        Column(
            Modifier
                .selectableGroup()
                .padding(vertical = 8.dp)
        ) {
            options.forEachIndexed { index, option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (option == selectedOption),
                            onClick = { selectedOption = option }
                        )
                        .padding(vertical = 6.dp)
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = { selectedOption = option }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = icons[index]),
                        contentDescription = option,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(option, color = Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(
                onClick = {
                    if (startTime.isValidTimeFormat() && endTime.isValidTimeFormat()) {
                        val id = System.currentTimeMillis().toInt()
                        val alarmActual = Alarm(id, startTime, endTime)
                        viewModel.insertAlarm(alarmActual)
                        alarmScheduler.scheduleAlarm(alarmActual)
                    }
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = colorGradient1),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text(stringResource(R.string.save), fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text(stringResource(R.string.close), fontSize = 18.sp)
            }
        }
    }
}


@Composable
fun DateTimePickerField(
    context: Context,
    label: String,
    value: String,
    iconRes: Int,
    onValueSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.LightGray.copy(alpha = 0.3f), shape = RoundedCornerShape(10.dp))
            .clickable {
                if (label.contains("Date")) showDatePicker(context, onValueSelected)
                else showTimePicker(context, onValueSelected)
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = value, fontSize = 16.sp, color = Color.Black)
    }
}

fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            onDateSelected("$dayOfMonth/${month + 1}/$year")
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).show()
}

fun showTimePicker(context: Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    TimePickerDialog(
        context,
        { _, hour, minute ->
            onTimeSelected("$hour:$minute")
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        true
    ).show()
}
