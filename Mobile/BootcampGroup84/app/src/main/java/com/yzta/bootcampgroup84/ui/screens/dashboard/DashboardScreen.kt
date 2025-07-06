package com.yzta.bootcampgroup84.ui.screens.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yzta.bootcampgroup84.ui.theme.VeryDarkGray

@Composable
fun DashboardPage(navController: NavController,modifier: Modifier = Modifier) {
    Surface(
        color = VeryDarkGray,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Dashboard Page",
                color = Color.White,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}