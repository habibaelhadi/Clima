package com.example.clima.composable.settings.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clima.MainActivity
import com.example.clima.R
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.utilites.setLocale
import com.example.clima.utilites.updateUnitsBasedOnLanguage

@Composable
fun SettingsScreen() {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val context = LocalContext.current
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        LanguageSettingsScreen(context, sharedPreferences)
        UnitsSettingsScreen(context, sharedPreferences)
        LocationSettingsScreen(context, sharedPreferences)
    }
}

@Composable
fun LanguageSettingsScreen(context: Context, sharedPreferences: SharedPreferences) {
    val language = sharedPreferences.getString("app_language", "English") ?: "English"

    val savedLanguage = remember { mutableStateOf(language) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = colorGradient1, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
    ) {
        Text(
            text = stringResource(R.string.language),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontWeight = FontWeight.Bold,
            color = Gray,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
        )

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        SettingRow(
            label = stringResource(R.string.selected_language),
            options = listOf("English", "العربية", "Türkiye"),
            defaultValue = savedLanguage // Correctly set the saved language
        ) { newLanguage ->
            sharedPreferences.edit().putString("app_language", newLanguage).apply()
            setLocale(context, newLanguage)

            updateUnitsBasedOnLanguage(context,sharedPreferences,newLanguage)

            // Restart activity to apply language change
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
    }
}

@Composable
fun LocationSettingsScreen(context: Context, sharedPreferences: SharedPreferences) {
    val gps = stringResource(R.string.gps)
    val location = sharedPreferences.getString("app_location", gps) ?: gps

    val savedLocation = remember { mutableStateOf(location) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = colorGradient1, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
    ) {
        Text(
            text = stringResource(R.string.location),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontWeight = FontWeight.Bold,
            color = Gray,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
        )

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        SettingRow(
            stringResource(R.string.selected_location), listOf(
                stringResource(R.string.gps),
                stringResource(R.string.map)
            ),
            defaultValue = savedLocation
        ) { newLocation ->
            sharedPreferences.edit().putString("app_location", newLocation).apply()
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
    }
}

@Composable
fun UnitsSettingsScreen(context: Context, sharedPreferences: SharedPreferences) {

    val celsius = stringResource(R.string.celsius_c)
    val fahrenheit = stringResource(R.string.fahrenheit_f)
    val miles = stringResource(R.string.miles_per_hour_m_h)
    val meters = stringResource(R.string.meters_per_second_m_s)

    var temp = sharedPreferences.getString("app_temp",
        celsius) ?: celsius
    var wind =  sharedPreferences.getString("app_wind",
        miles) ?: miles

    val savedTemp = remember { mutableStateOf(temp) }
    val savedWind = remember { mutableStateOf(wind) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color = colorGradient1, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
    ) {
        Text(
            text = stringResource(R.string.units),
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontWeight = FontWeight.Bold,
            color = Gray,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
        )

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        // Temperature Setting
        SettingRow(
            stringResource(R.string.temperature),
            listOf(
                stringResource(R.string.celsius_c),
                stringResource(R.string.fahrenheit_f),
                stringResource(R.string.kelvin_k)
            ),
            defaultValue = savedTemp
        ) { newTemp ->
            savedTemp.value = newTemp
            sharedPreferences.edit().putString("app_temp", newTemp).apply()

            // Automatically update wind speed unit based on temperature unit
            val updatedWind = when (newTemp) {
                fahrenheit -> miles
                else -> meters
            }

            if (updatedWind != savedWind.value) {
                savedWind.value = updatedWind
                sharedPreferences.edit().putString("app_wind", updatedWind).apply()
            }
        }

        // Wind Speed Setting
        SettingRow(
            stringResource(R.string.wind),
            listOf(
                stringResource(R.string.miles_per_hour_m_h),
                stringResource(R.string.meters_per_second_m_s)
            ),
            defaultValue = savedWind
        ) { newWind ->
            savedWind.value = newWind
            sharedPreferences.edit().putString("app_wind", newWind).apply()

            val updateTemp = when(newWind){
                miles -> fahrenheit
                else -> celsius

            }

            if(updateTemp != savedTemp.value){
                savedTemp.value = updateTemp
                sharedPreferences.edit().putString("app_temp", updateTemp).apply()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

    }
}

@Composable
fun SettingRow(
    label: String,
    options: List<String>,
    defaultValue: MutableState<String>,
    onValueChange: (String) -> Unit = {}
) {

    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    val settingKey = when (label) {
        stringResource(R.string.selected_language) -> "app_language"
        stringResource(R.string.selected_location) -> "app_location"
        stringResource(R.string.temperature) -> "app_temp"
        stringResource(R.string.wind) -> "app_wind"
        else -> label // Fallback to using label, but it's not recommended
    }

    LaunchedEffect(Unit) {
        defaultValue.value = sharedPreferences.getString(settingKey, defaultValue.value) ?: defaultValue.value
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Gray,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Box {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = defaultValue.value,
                    fontSize = 14.sp,
                    fontFamily = FontFamily(Font(R.font.exo2)),
                    color = Gray
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown",
                    tint = Gray,
                    modifier = Modifier.size(20.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option, fontFamily = FontFamily(Font(R.font.exo2))) },
                        onClick = {
                            defaultValue.value = option
                            expanded = false
                            sharedPreferences.edit().putString(label, option).apply()
                            onValueChange(option)
                        },
                        leadingIcon = {
                            if (defaultValue.value == option) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.Blue
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}
