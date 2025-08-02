package com.example.sp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sp.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(onGetStartedClick = {
                navController.navigate("onboarding")
            })
        }

        composable("onboarding") {
            OnboardingPager(navController = navController)
        }

        composable("signup") {
            SignUpScreen(navController = navController)
        }

        composable("signin") {
            SignInScreen(navController = navController)
        }

        composable("dashboard") {
            DashboardScreen(navController = navController)
        }

        composable("journal") {
            JournalScreen(navController = navController)
        }

        composable("settings") {
            SettingsScreen(navController = navController)
        }
    }
}
