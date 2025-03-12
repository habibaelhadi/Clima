package com.example.clima.routes

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


@Composable
fun SetupNavHost(){
    var navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = RoutesScreens.Home.route
    ){
        composable(RoutesScreens.Home.route){
        }

        composable(RoutesScreens.Favourites.route){
        }

        composable(RoutesScreens.Alarms.route){
        }

        composable(RoutesScreens.Settings.route){
        }

    }
}
