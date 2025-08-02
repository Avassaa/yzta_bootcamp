package com.example.sp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sp.R

@Composable
fun BottomNavBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color(0xFF2A1102))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 36.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.nav_dashboard_icon),
                contentDescription = "Dashboard",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        navController.navigate("dashboard")
                    },
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(36.dp))

            Icon(
                painter = painterResource(id = R.drawable.nav_settings_icon),
                contentDescription = "Settings",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        navController.navigate("settings")
                    },
                tint = Color.Unspecified
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp)
                .size(90.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .background(Color(0xFF2A1102)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(6.dp, Color(0xFFFFFF89), CircleShape)
                    .background(Color(0xFF2A1102))
                    .clickable {
                        navController.navigate("journal")
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.nav_camera__icon),
                    contentDescription = "Camera",
                    modifier = Modifier.size(52.dp),
                    tint = Color.Unspecified
                )
            }
        }
    }
}
