package com.yzta.bootcampgroup84.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yzta.bootcampgroup84.interfaces.Screens
import com.yzta.bootcampgroup84.ui.screens.dashboard.DashboardPage
import com.yzta.bootcampgroup84.ui.screens.login.LoginPage
@Composable
fun NavigationGraph(navController: NavHostController,  paddingValues: PaddingValues) {
    NavHost(navController = navController, startDestination = Screens.LoginScreen.screenName) {
        composable(Screens.LoginScreen.screenName) {
            LoginPage(
                navController = navController,
                modifier = Modifier.padding(),
            )
        }
        composable(Screens.DashboardScreen.screenName) { navBackStackEntry ->

                DashboardPage(
                    navController = navController,
                    modifier = Modifier.padding(paddingValues),
                )
            }
        }
    }
