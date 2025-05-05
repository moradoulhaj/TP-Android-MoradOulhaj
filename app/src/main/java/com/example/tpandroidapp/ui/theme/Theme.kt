package com.example.tpandroidapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Define your colors here
private val lightColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    secondary = Color(0xFF03DAC5),
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun TPAndroidAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
