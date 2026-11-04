package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AmberCopperBright,
    onPrimary = IndustrialNavyDark,
    primaryContainer = IndustrialNavyCard,
    onPrimaryContainer = AmberCopperLight,
    secondary = IndustrialSteelBlue,
    onSecondary = Color.White,
    secondaryContainer = IndustrialBorder,
    onSecondaryContainer = Color(0xFFE0E1DD),
    tertiary = AmberCopperPrimary,
    background = IndustrialNavyDark,
    onBackground = TextPrimaryDark,
    surface = IndustrialNavySurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = IndustrialNavyCard,
    onSurfaceVariant = TextSecondaryDark,
    error = StatusDangerRed,
    onError = Color.White,
    outline = IndustrialBorder
)

private val LightColorScheme = lightColorScheme(
    primary = AmberCopperPrimary,
    onPrimary = Color.White,
    primaryContainer = AmberCopperLight,
    onPrimaryContainer = BrassDark,
    secondary = IndustrialSteelBlue,
    onSecondary = Color.White,
    secondaryContainer = CleanIvoryCard,
    onSecondaryContainer = IndustrialNavyDark,
    tertiary = BrassDark,
    background = CleanIvoryBackground,
    onBackground = TextPrimaryLight,
    surface = CleanIvorySurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = CleanIvoryCard,
    onSurfaceVariant = TextSecondaryLight,
    error = StatusDangerRed,
    onError = Color.White,
    outline = CleanIvoryBorder
)

@Composable
fun AurelionTheme(
    darkTheme: Boolean = true, // Default to premium industrial dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
