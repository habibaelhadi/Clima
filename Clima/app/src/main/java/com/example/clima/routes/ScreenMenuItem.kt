package com.example.clima.routes

import androidx.annotation.DrawableRes
import com.example.clima.R

data class ScreenMenuItem(
    val screen: RoutesScreens,
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val id: Int
){
    companion object{
        val menuItems = listOf(
            ScreenMenuItem(RoutesScreens.Home, RoutesScreens.Home.icon, R.drawable.ic_baseline_home_avd, 1),
            ScreenMenuItem(RoutesScreens.Favourites,RoutesScreens.Favourites.icon, R.drawable.ic_baseline_favourite_avd,2),
            ScreenMenuItem(RoutesScreens.Alarms, RoutesScreens.Alarms.icon, R.drawable.ic_baseline_notification_avd,3),
            ScreenMenuItem(RoutesScreens.Settings, RoutesScreens.Settings.icon, R.drawable.ic_baseline_settings_avd,4),)
    }
}