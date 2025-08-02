package com.example.sp.ui.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sp.ui.screens.Onboarding1Screen
import com.example.sp.ui.screens.SplashScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen {
                navController.navigate("onboarding1")
            }
        }
        composable("onboarding1") {
            Onboarding1Screen()
        }
    }
}
