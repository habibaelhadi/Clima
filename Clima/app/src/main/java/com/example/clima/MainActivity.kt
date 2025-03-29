package com.example.clima

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.clima.composableFunctions.alarms.service.AlarmService
import com.example.clima.composableFunctions.alarms.view.createNotificationChannel
import com.example.clima.model.CurrentWeather
import com.example.clima.model.FavouritePOJO
import com.example.clima.model.ForeCast
import com.example.clima.routes.ScreenMenuItem
import com.example.clima.routes.SetupNavHost
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.White
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.utilites.setLocale
import com.google.gson.Gson
import kotlinx.coroutines.delay
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
        }

        val forecast = ForeCast(
            city = ForeCast.City(
                coord = ForeCast.Coord(lat = 30.0444, lon = 31.2357),
                country = "Egypt",
                id = 12345,
                name = "Cairo",
                population = 9500000,
                sunrise = 1680000000,
                sunset = 1680043200,
                timezone = 7200
            ),
            cnt = 5,
            cod = "200",
            list = listOf(
                ForeCast.ForecastWeather(
                    clouds = ForeCast.Clouds(all = 40),
                    dt = 1680000000,
                    dt_txt = "2024-03-29 12:00:00",
                    main = ForeCast.Main(
                        feels_like = 30.5,
                        grnd_level = 1000,
                        humidity = 60,
                        pressure = 1012,
                        sea_level = 1015,
                        temp = 28.3,
                        temp_kf = 0.5,
                        temp_max = 29.0,
                        temp_min = 27.5
                    ),
                    pop = 0.1,
                    rain = ForeCast.Rain(`3h` = 0.0),
                    sys = ForeCast.Sys(pod = "d"),
                    visibility = 10000,
                    weather = listOf(
                        ForeCast.Weather(
                            description = "clear sky",
                            icon = "01d",
                            id = 800,
                            main = "Clear"
                        )
                    ),
                    wind = ForeCast.Wind(deg = 180, gust = 3.5, speed = 5.0),
                    snow = ForeCast.Snow(4.0)
                )
            ),
            message = 0
        )



        val currentWeather = CurrentWeather(
            base = "stations",
            clouds = CurrentWeather.Clouds(all = 20),
            cod = 200,
            coord = CurrentWeather.Coord(lat = 30.0444, lon = 31.2357),
            dt = 1680000000,
            id = 98765,
            main = CurrentWeather.Main(
                feels_like = 32.0,
                grnd_level = 1000,
                humidity = 55,
                pressure = 1013,
                sea_level = 1015,
                temp = 30.0,
                temp_max = 31.5,
                temp_min = 28.0
            ),
            name = "Cairo",
            sys = CurrentWeather.Sys(
                country = "EG",
                id = 1,
                sunrise = 1680000000,
                sunset = 1680043200,
                type = 1
            ),
            timezone = 7200,
            visibility = 10000,
            weather = listOf(
                CurrentWeather.Weather(
                    description = "few clouds",
                    icon = "02d",
                    id = 801,
                    main = "Clouds"
                )
            ),
            wind = CurrentWeather.Wind(deg = 150, gust = 4.0, speed = 6.5)
        )

        val locationData = FavouritePOJO(
            latitude = 30.0444,
            longitude = 31.2357,
            currentWeather = currentWeather,
            forecast = forecast,
            country = "Egypt",
            city = "Cairo"
        )

        val serviceIntent = Intent(this, AlarmService::class.java).apply {
            putExtra("location", Gson().toJson(locationData)) // Convert to JSON before sending
        }

        startService(serviceIntent)

        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("app_language", "English") ?: "English"
        val skipSplash = sharedPreferences.getBoolean("skip_splash", false)

        setLocale(this, savedLanguage)

        setContent {
            var showSplash by remember { mutableStateOf(!skipSplash) }

            val navController = rememberNavController()

            val deepLinkUri = intent?.data?.toString()

            LaunchedEffect(deepLinkUri) {
                deepLinkUri?.let { uri ->
                    if (uri.startsWith("D:\\ITI\\Android using Kotlin\\Project\\Clima\\app\\src\\main\\java\\com\\example\\clima\\composableFunctions\\details\\view")) {
                        val jsonString = uri.substringAfter("favDetails/")
                        navController.navigate("favDetails/$jsonString")
                    }
                }
            }

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                if (showSplash) {
                    SplashScreen(
                        onAnimationComplete = {
                            showSplash = false
                        }
                    )
                } else {
                    ScaffoldSample()
                }
            }
        }

        sharedPreferences.edit().putBoolean("skip_splash", false).apply()
    }

    @Composable
    fun ScaffoldSample() {
        val navController = rememberNavController()
        val snackbar = remember { SnackbarHostState() }
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackbar)
            },
            bottomBar = {
                CurvedNavBar(navController)
            },
            content = { innerPadding ->
                Column(
                    Modifier.padding(innerPadding)
                ) {
                    SetupNavHost(navController, snackbar)
                }
            }
        )
    }

    @Composable
    fun SplashScreen(
        onAnimationComplete: () -> Unit // Callback to notify when the animation is complete
    ) {
        val pacificoFontFamily = FontFamily(
            Font(R.font.pacifico_regular) // Replace with your font file name
        )
        // Load the Lottie composition
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(R.raw.splash)
        )

        // Control the animation progress
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever // Loop the animation
        )

        LaunchedEffect(Unit) {
            delay(4000) // Display the splash screen for 3 seconds
            onAnimationComplete()
        }

        // Display the Lottie animation
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Gray),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.fillMaxSize(0.7f) // Adjust size as needed
            )
            Spacer(modifier = Modifier.padding(32.dp))
            Text(
                text = stringResource(R.string.clima),
                fontFamily = pacificoFontFamily,
                fontSize = 40.sp,
                color = colorGradient1
            )
        }
    }

    @Composable
    fun CurvedNavBar(navController: NavHostController) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            factory = { context ->
                CurvedBottomNavigationView(context).apply {

                    unSelectedColor = White.toArgb()
                    selectedColor = colorGradient1.toArgb()
                    navBackgroundColor = colorGradient1.toArgb()

                    val cbnMenuItems = ScreenMenuItem.menuItems.map { screen ->
                        CbnMenuItem(
                            icon = screen.icon,
                            avdIcon = screen.selectedIcon,
                            destinationId = screen.id
                        )
                    }
                    layoutDirection = View.LAYOUT_DIRECTION_LTR
                    setMenuItems(cbnMenuItems.toTypedArray(), 0)
                    setOnMenuItemClickListener { cbnMenuItem, i ->
                        navController.popBackStack()
                        navController.navigate(ScreenMenuItem.menuItems[i].screen.route)
                    }
                }
            }
        )
    }
}
