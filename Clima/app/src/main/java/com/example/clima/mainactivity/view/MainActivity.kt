package com.example.clima.mainactivity.view

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.collectAsState
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
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.clima.R
import com.example.clima.composable.alarms.broadcastrecievers.AlarmBroadcastReceiver
import com.example.clima.local.AppDataBase
import com.example.clima.local.SharedPreferencesDataSource
import com.example.clima.local.WeatherLocalDataSource
import com.example.clima.mainactivity.viewmodel.MainActivityViewModel
import com.example.clima.remote.RetrofitProduct
import com.example.clima.remote.WeatherRemoteDataSource
import com.example.clima.repo.WeatherRepo
import com.example.clima.routes.ScreenMenuItem
import com.example.clima.routes.SetupNavHost
import com.example.clima.ui.theme.Gray
import com.example.clima.ui.theme.White
import com.example.clima.ui.theme.colorGradient1
import com.example.clima.utilites.ConnectivityObserver
import com.example.clima.utilites.MyMediaPlayer
import com.example.clima.utilites.setLocale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView

const val REQUEST_LOCATION_CODE = 2005

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient

   private lateinit var viewModel: MainActivityViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weatherRepo = WeatherRepo.getInstance(
            WeatherRemoteDataSource(RetrofitProduct.retrofit),
            WeatherLocalDataSource(AppDataBase.getInstance(this).weatherDao()),
            SharedPreferencesDataSource.getInstance(this)
        )

        viewModel = MainActivityViewModel(weatherRepo)


        val savedLanguage = viewModel.getLanguageForLocale()
        val skipSplash = viewModel.getSplashStateForActivity()
        setLocale(this, savedLanguage)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val intentFilterReceiver = IntentFilter("ACTION")
        registerReceiver(AlarmBroadcastReceiver(), intentFilterReceiver,
            RECEIVER_EXPORTED)

        MyMediaPlayer.stopAudio()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()

        val connectivityObserver = ConnectivityObserver(applicationContext)

        setContent {
            var showSplash by remember { mutableStateOf(!skipSplash) }


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
                    ScaffoldSample(connectivityObserver)
                }
            }
        }

        viewModel.setSplashState(false)
    }

    override fun onStart() {
        super.onStart()
        if(viewModel.getLocation()
            == getString(R.string.gps)){
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    getFreshLocation()
                } else {
                    enableLocationServices()
                }
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    REQUEST_LOCATION_CODE
                )
            }
        }
        viewModel.getLocationSourceFlow(getString(R.string.gps)) {
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    getFreshLocation()
                } else {
                    enableLocationServices()
                }
            }
        }
        viewModel.getLocationChangeFlow()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_LOCATION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getFreshLocation()
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun getFreshLocation(){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                viewModel.setMapCoordinate(location.latitude.toString(),
                    location.longitude.toString()  )
                viewModel.getLocationChangeFlow()
            }
        }
    }

    private fun checkPermissions() : Boolean{
        return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableLocationServices(){
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun isLocationEnabled() : Boolean{
        val locationManager : LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @Composable
    fun ScaffoldSample(connectivityObserver: ConnectivityObserver) {
        val navController = rememberNavController()
        val snackbar = remember { SnackbarHostState() }
        val isConnected by connectivityObserver.isConnected.collectAsState()

        Scaffold(
            topBar = {
                if (!isConnected) {
                    OfflineBanner()
                }
            },
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
        onAnimationComplete: () -> Unit
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
    fun OfflineBanner() {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.error)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.offline_mode),
                color = MaterialTheme.colorScheme.onError,
                fontSize = 16.sp
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