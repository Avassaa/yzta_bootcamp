package com.example.sp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sp.ui.screens.*

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(onGetStartedClick = {
                navController.navigate("onboarding") //dwfault onboarding
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
        composable(
            route = "analysis/{entryId}",
            arguments = listOf(navArgument("entryId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Extract the entryId from the route
            val entryId = backStackEntry.arguments?.getString("entryId")
            if (entryId != null) {
                AnalysisScreen(navController = navController, entryId = entryId)
            }
        }
    }
}
