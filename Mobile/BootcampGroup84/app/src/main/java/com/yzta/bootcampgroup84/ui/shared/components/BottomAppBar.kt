package com.yzta.bootcampgroup84.ui.shared.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.yzta.bootcampgroup84.interfaces.NavItem
import com.yzta.bootcampgroup84.interfaces.Screens
import com.yzta.bootcampgroup84.ui.theme.BabyBlue
import com.yzta.bootcampgroup84.ui.theme.BottomBarDarkGray
import com.yzta.bootcampgroup84.ui.theme.StressBabyBlue

@Composable
fun BottomAppBar(navController: NavController, modifier: Modifier = Modifier) {
    val navItemList = listOf(
        NavItem("Dashboard", Icons.Default.Home, 3),
        NavItem("Menu", Icons.Default.Menu, 5),
        NavItem("Settings", Icons.Default.Settings, 0),
    )

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(containerColor = BottomBarDarkGray) {
        navItemList.forEachIndexed { index, navItem ->
            var selected = when(index) {
                0 -> currentRoute == Screens.DashboardScreen.screenName == true
                1 -> currentRoute == Screens.DashboardScreen.screenName == true
                2 -> currentRoute == Screens.DashboardScreen.screenName == true
                else -> false
            }

            NavigationBarItem(
                selected = selected,
                onClick = {
                    when(index) {
                        0 -> {
                            if (!selected) {
                                navController.navigate(Screens.DashboardScreen.screenName) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }
                        1 -> {
                            if (!selected) {
                                navController.navigate(Screens.DashboardScreen.screenName) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }
                        2 -> {
                            if (!selected) {
                                navController.navigate(Screens.DashboardScreen.screenName) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = BabyBlue,
                    selectedTextColor = BabyBlue,
                    selectedIndicatorColor = StressBabyBlue,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White,
                    disabledIconColor = Color.Gray,
                    disabledTextColor = Color.Gray
                ),
                icon = {
                    BadgedBox(badge = {
                        if(navItem.badgeCount > 0)
                            Badge {
                                Text(text = navItem.badgeCount.toString())
                            }
                    }) {
                        Icon(imageVector = navItem.icon, contentDescription = "Icon")
                    }
                },
                label = {
                    Text(text = navItem.label)
                }
            )
        }
    }
}