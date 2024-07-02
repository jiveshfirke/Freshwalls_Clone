package com.dedsec.freshwalls.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.dedsec.freshwalls.navigation.Screen
import kotlinx.coroutines.time.delay
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SplashScreen(navHostController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(duration = Duration.ofSeconds(3))
        navHostController.navigate(Screen.AuthenticationScreen.route) {
            popUpTo(Screen.SplashScreen.route) { inclusive = true }
        }
    }
    
    Text(text = "Hello Splash screen")
}