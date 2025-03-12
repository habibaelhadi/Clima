package com.example.clima.utilites

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


enum class NavigationBarItems(val icon: ImageVector){
    HOME(icon = Icons.Filled.Home),
    FAVOURITES(icon = Icons.Filled.Favorite),
    ALARMS(icon = Icons.Filled.Notifications),
    SETTINGS(icon = Icons.Filled.Settings)
}