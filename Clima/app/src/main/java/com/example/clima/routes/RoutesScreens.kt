package com.example.clima.routes

import com.example.clima.R
import kotlinx.serialization.Serializable

@Serializable
sealed class RoutesScreens (val route: String, val label: String, val icon: Int){

    @Serializable
    object Home : RoutesScreens("home","Home", R.drawable.home)

    @Serializable
    object Favourites : RoutesScreens("favourites","Favourites", R.drawable.heart)

    @Serializable
    object Alarms : RoutesScreens("alarms","Alarms", R.drawable.notification)

    @Serializable
    object Settings : RoutesScreens("settings","Settings", R.drawable.settings)
}