package com.example.clima

import android.os.Bundle
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
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
import com.example.clima.routes.ScreenMenuItem
import com.example.clima.routes.SetupNavHost
import com.example.clima.utilites.ColorResource
import kotlinx.coroutines.delay
import np.com.susanthapa.curved_bottom_navigation.CbnMenuItem
import np.com.susanthapa.curved_bottom_navigation.CurvedBottomNavigationView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                var showSplash by remember { mutableStateOf(true) }

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
    }

    @Preview(showBackground = true)
    @Composable
    fun ScaffoldSample() {
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                CurvedNavBar(navController)
            },
            content = { innerPadding ->
                Column(
                    Modifier.padding(innerPadding)
                ) {
                    SetupNavHost(navController)
                }
            }
        )
    }

    @Composable
    fun SplashScreen(
        onAnimationComplete: () -> Unit // Callback to notify when the animation is complete
    ) {
        val backgroundColor: Color = colorResource(id = R.color.grey)
        val textColor: Color = colorResource(id = R.color.purple)
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
            modifier = Modifier.fillMaxSize()
                .background(backgroundColor),
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
                color = textColor)
        }
    }

    @Composable
    fun CurvedNavBar(navController: NavHostController) {
        AndroidView(
            factory = { context ->
                CurvedBottomNavigationView(context).apply {

                    unSelectedColor = ColorResource.WHITE.getColor(context)
                    selectedColor = ColorResource.PURPLE.getColor(context)
                    navBackgroundColor = ColorResource.PURPLE.getColor(context)

                    val cbnMenuItems = ScreenMenuItem.menuItems.map { screen ->
                        CbnMenuItem(
                            icon = screen.icon,
                            avdIcon = screen.selectedIcon,
                            destinationId = screen.id
                        )
                    }
                    setMenuItems(cbnMenuItems.toTypedArray(), 0)
                    setOnMenuItemClickListener{ cbnMenuItem, i ->
                         navController.popBackStack()
                         navController.navigate(ScreenMenuItem.menuItems[i].route.route)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        )
    }
}
