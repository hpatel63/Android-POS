package com.polaris.hospitalitypos.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColorScheme(
    primary = Color(0xFF00C2FF),
    onPrimary = Color(0xFF00202E),
    background = Color(0xFF04111A),
    surface = Color(0xFF0E2A3B),
    secondary = Color(0xFF7A5CFF)
)

private val LightColorPalette = lightColorScheme(
    primary = Color(0xFF00617D),
    onPrimary = Color.White,
    background = Color(0xFFF2FAFF),
    surface = Color(0xFFFFFFFF),
    secondary = Color(0xFF6750A4)
)

@Composable
fun HospitalityTheme(
    darkTheme: Boolean = true,
    colorScheme: ColorScheme = if (darkTheme || isSystemInDarkTheme()) DarkColorPalette else LightColorPalette,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
