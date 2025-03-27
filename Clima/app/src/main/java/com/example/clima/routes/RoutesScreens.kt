package com.example.clima.routes

import com.example.clima.R
import kotlinx.serialization.Serializable

@Serializable
sealed class RoutesScreens (val route: String, val label: String, val icon: Int){

    @Serializable
    object Home : RoutesScreens("home","Home", R.drawable.baseline_home_24)

    @Serializable
    object Favourites : RoutesScreens("favourites","Favourites", R.drawable.baseline_favorite_24)

    @Serializable
    object Alarms : RoutesScreens("alarms","Alarms", R.drawable.baseline_notifications_active_24)

    @Serializable
    object Settings : RoutesScreens("settings","Settings", R.drawable.baseline_settings_24)

    @Serializable
    object Map : RoutesScreens("map","Map", R.drawable.baseline_location_pin_24)

    @Serializable
    object FavouriteDetails : RoutesScreens("favDetails/{lat}/{lng}", "Details", R.drawable.country) {
        fun route(lat: Double, lng: Double) = "favDetails/$lat/$lng"
    }
}