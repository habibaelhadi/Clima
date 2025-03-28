package com.example.clima.routes

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
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
import com.example.clima.model.FavouritePOJO
import com.google.gson.Gson


@Composable
fun SetupNavHost(
    navController: NavHostController,
    snackbar: SnackbarHostState
){
    NavHost(
        navController = navController,
        startDestination = RoutesScreens.Home.route
    ){
        composable(RoutesScreens.Home.route){
            HomeScreen()
        }

        composable(RoutesScreens.Favourites.route){
            FavouritesScreen( snackbar,
                navigateToMap = {
                    navController.navigate(RoutesScreens.Map.route)
                },
                navigateToDetails = {location ->
                    val gson = Gson()
                    val jsonString = gson.toJson(location)
                navController.navigate(RoutesScreens.FavouriteDetails.route(jsonString))
            })
        }

        composable(RoutesScreens.Alarms.route){
            AlarmsScreen()
        }

        composable(RoutesScreens.Settings.route){
            SettingsScreen()
        }

        composable(RoutesScreens.Map.route){
            MapScreen()
        }

        composable(
            route = RoutesScreens.FavouriteDetails.route,
            arguments = listOf(
                navArgument("location") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val jsonString = backStackEntry.arguments?.getString("location")
            val location = try {
                Gson().fromJson(jsonString, FavouritePOJO::class.java)
            } catch (e: Exception) {
                null
            }
            if (location != null) {
                Log.i("TAG", "SetupNavHost: ${location.forecast.list}")
                DetailsScreen(location)
            } else {
                Log.e("TAG", "Location data is null")
            }
        }
    }
}
