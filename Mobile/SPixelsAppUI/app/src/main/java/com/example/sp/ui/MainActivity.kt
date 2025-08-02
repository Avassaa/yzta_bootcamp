package com.example.sp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.example.sp.ui.navigation.AppNavGraph // Bu senin navigation klasöründeki nav graph
import com.example.sp.ui.theme.SPtheme // Tema dosyan buradaysa

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()

            SPtheme { // Kendi temanı kullan
                AppNavGraph(navController = navController)
            }
        }
    }
}
