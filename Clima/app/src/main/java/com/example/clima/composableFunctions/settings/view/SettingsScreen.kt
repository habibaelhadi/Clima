package com.example.clima.composableFunctions.settings.view

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clima.R
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.colorGradient1

@Composable
fun SettingsScreen(showFAB: MutableState<Boolean>) {
    showFAB.value = false
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        LanguageSettingsScreen()
        UnitsSettingsScreen()
        LocationSettingsScreen()
    }
}

@Composable
fun LanguageSettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color =  colorGradient1, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
    ) {
        Text(
            text = "Language",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontWeight = FontWeight.Bold,
            color = Gray,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
        )

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        SettingRow("Selected Language", listOf("English", "العربية"))

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

    }
}

@Composable
fun LocationSettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color =  colorGradient1, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
    ) {
        Text(
            text = "Location",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontWeight = FontWeight.Bold,
            color = Gray,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
        )

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        SettingRow("Selected Location", listOf("GPS", "Map"))

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

    }
}

@Composable
fun UnitsSettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(color =  colorGradient1, shape = RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFFE0E0E0), shape = RoundedCornerShape(16.dp))
    ) {
        Text(
            text = "Units",
            fontSize = 14.sp,
            fontFamily = FontFamily(Font(R.font.exo2)),
            fontWeight = FontWeight.Bold,
            color = Gray,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
        )

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        SettingRow("Temperature", listOf("Celsius (°C)", "Fahrenheit (°F)", "Kelvin (°K)"))
        SettingRow("Wind", listOf("Kilometers per hour (km/h)", "Miles per hour (mph)"))

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

    }
}

@Composable
fun SettingRow(label: String, options: List<String>) {
    var selectedValue by remember { mutableStateOf(options.first()) }
    var expanded by remember { mutableStateOf(false) }

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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedValue,
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
                        text = { Text(option,
                            fontFamily = FontFamily(Font(R.font.exo2))) },
                        onClick = {
                            selectedValue = option
                            expanded = false
                        },
                        leadingIcon = {
                            if (selectedValue == option) {
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
