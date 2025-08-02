package com.example.sp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.pager.*
import androidx.navigation.NavController
import com.example.sp.ui.components.BottomNavBar

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPager(navController: NavController) {
    val pagerState = rememberPagerState()

    HorizontalPager(
        count = 4, // 3 onboarding + 1 signup
        state = pagerState,
        modifier = Modifier.fillMaxSize()
    ) { page ->
        when (page) {
            0 -> Onboarding1Screen()
            1 -> OnboardingScreen2()
            2 -> OnboardingScreen3()
            3 -> SignUpScreen(navController = navController)
        }
    }
}
