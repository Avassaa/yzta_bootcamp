package com.example.sp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sp.R

@Composable
fun SplashScreen(onGetStartedClick: () -> Unit) {
    val interFont = FontFamily(Font(R.font.inter_regular))

    Box(modifier = Modifier.fillMaxSize()) {

        // 🔹 Arka plan
        Image(
            painter = painterResource(id = R.drawable.ic_girl_smile),
            contentDescription = "Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Logo sabit konumda
        Image(
            painter = painterResource(id = R.drawable.ic_logo_ring),
            contentDescription = "Logo Ring",
            modifier = Modifier
                .size(250.dp) // Sabit boyut
                .align(Alignment.BottomCenter)
                .offset(y = (-310).dp) // Ekranın altından yukarıya 354dp kadar yukarı taşı
        )

        // 🔹 Başlık ve alt başlık
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 42.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "A picture of your mind",
                fontSize = 24.sp,
                color = Color(0xFF281000),
                fontFamily = interFont,
                modifier = Modifier.padding(bottom = 290.dp) // Burayı ihtiyacına göre ayarlayabilirsin
            )
        }


        // 🔹 Buton sabit
        Button(
            onClick = onGetStartedClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 95.dp)
                .width(320.dp)
                .height(60.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A1102))
        ) {
            Text(
                text = "Get started",
                color = Color(0xFFF9FD89),
                fontSize = 24.sp,
                fontFamily = interFont,
                fontWeight = FontWeight.Medium
            )
        }
    }
}