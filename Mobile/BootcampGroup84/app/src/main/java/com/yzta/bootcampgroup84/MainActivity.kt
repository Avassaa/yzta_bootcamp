package com.yzta.bootcampgroup84

import CollapsibleTopAppBar
import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yzta.bootcampgroup84.interfaces.Screens
import com.yzta.bootcampgroup84.ui.navigation.NavigationGraph
import com.yzta.bootcampgroup84.ui.shared.components.BottomAppBar
import com.yzta.bootcampgroup84.ui.theme.BootcampGroup84Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),

                topBar = {
                    if (currentRoute != Screens.LoginScreen.screenName
                 ) {
                        CollapsibleTopAppBar(
                            navController = navController,
                            scrollBehavior = scrollBehavior
                        )
                    }},
                bottomBar = {
                    if (currentRoute != Screens.LoginScreen.screenName  ) {
                        BottomAppBar(navController = navController, modifier = Modifier.padding(8.dp))
                    }
                }
            ) { paddingValues->
                NavigationGraph(
                    navController = navController,
                    paddingValues=paddingValues
                )
                }
            }

        }
    }
