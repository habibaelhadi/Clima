package com.example.clima.routes

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.clima.composable.alarms.view.AlarmsScreen
import com.example.clima.composable.details.view.DetailsScreen
import com.example.clima.composable.favourites.view.FavouritesScreen
import com.example.clima.composable.home.view.HomeScreen
import com.example.clima.composable.map.view.MapScreen
import com.example.clima.composable.settings.view.SettingsScreen
import com.example.clima.model.FavouritePOJO
import com.google.gson.Gson


@Composable
fun SetupNavHost(
    navController: NavHostController,
    snackbar: SnackbarHostState
) {
    NavHost(
        navController = navController,
        startDestination = RoutesScreens.Home.route
    ) {
        composable(RoutesScreens.Home.route) {
            HomeScreen()
        }

        composable(RoutesScreens.Favourites.route) {
            FavouritesScreen(snackbar,
                navigateToMap = {
                    navController.navigate(RoutesScreens.Map.route(false))
                },
                navigateToDetails = { location ->
                    val gson = Gson()
                    val jsonString = gson.toJson(location)
                    navController.navigate(RoutesScreens.FavouriteDetails.route(jsonString))
                })
        }

        composable(RoutesScreens.Alarms.route) {
            AlarmsScreen(snackbar)
        }

        composable(RoutesScreens.Settings.route) {
            SettingsScreen { isSettings ->
                navController.navigate(RoutesScreens.Map.route(isSettings))
            }
        }

        composable(
            route = RoutesScreens.Map.route,
            arguments = listOf( navArgument("isSettings"){type = NavType.BoolType})
        ) {
            backStackEntry ->
            val isSettings = backStackEntry.arguments?.getBoolean("isSettings") ?: false
            MapScreen(isSettings)
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
