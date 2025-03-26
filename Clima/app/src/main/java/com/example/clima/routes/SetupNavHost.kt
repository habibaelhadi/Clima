package com.example.clima.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.clima.composableFunctions.alarms.view.AlarmsScreen
import com.example.clima.composableFunctions.details.view.DetailsScreen
import com.example.clima.composableFunctions.favourites.view.FavouritesScreen
import com.example.clima.composableFunctions.home.view.HomeScreen
import com.example.clima.composableFunctions.map.view.MapScreen
import com.example.clima.composableFunctions.settings.view.SettingsScreen


@Composable
fun SetupNavHost(navController: NavHostController,
                 showFAB : MutableState<Boolean>){
    NavHost(
        navController = navController,
        startDestination = RoutesScreens.Home.route
    ){
        composable(RoutesScreens.Home.route){
            HomeScreen(showFAB)
        }

        composable(RoutesScreens.Favourites.route){
            FavouritesScreen(showFAB,
                navigateToDetails = {lat,lng ->
                navController.navigate(RoutesScreens.FavouriteDetails.route(lat,lng))
            })
        }

        composable(RoutesScreens.Alarms.route){
            AlarmsScreen(showFAB)
        }

        composable(RoutesScreens.Settings.route){
            SettingsScreen(showFAB)
        }

        composable(RoutesScreens.Map.route){
            MapScreen()
        }

        composable(
            route = RoutesScreens.FavouriteDetails.route,
            arguments = listOf(
                navArgument("lat") { type = NavType.FloatType },
                navArgument("lng") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val latitude = backStackEntry.arguments?.getFloat("lat") ?: 30.0444
            val longitude = backStackEntry.arguments?.getFloat("lng") ?: 31.2357
            DetailsScreen(showFAB, latitude, longitude)
        }
    }
}
