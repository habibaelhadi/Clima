package com.example.clima.routes

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clima.composableFunctions.alarms.view.AlarmsScreen
import com.example.clima.composableFunctions.favourites.view.FavouritesScreen
import com.example.clima.composableFunctions.home.view.HomeScreen
import com.example.clima.composableFunctions.settings.view.SettingsScreen


@Composable
fun SetupNavHost(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = RoutesScreens.Home.route
    ){
        composable(RoutesScreens.Home.route){
            HomeScreen()
        }

        composable(RoutesScreens.Favourites.route){
            FavouritesScreen()
        }

        composable(RoutesScreens.Alarms.route){
            AlarmsScreen()
        }

        composable(RoutesScreens.Settings.route){
            SettingsScreen()
        }

    }
}
