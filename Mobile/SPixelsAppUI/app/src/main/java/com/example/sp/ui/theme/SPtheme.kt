package com.example.sp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFF2A1102),
    onPrimary = Color(0xFFF9FD89),
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF2A1102),
    onPrimary = Color(0xFFF9FD89),
)

@Composable
fun SPtheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,  // 👈 burası düzeltildi
        content = content
    )
}

