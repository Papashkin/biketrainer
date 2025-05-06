package com.antsfamily.biketrainer.ui.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val primaryColor = Color(0xFF38BDFF)
val primaryVariantColor = Color(0xFF6371FF)
val onPrimaryColor = Color(0xFFFFFFFF)
val secondaryColor = Color(0xFFF4F4FB)
val errorColor = Color(0xFFF44336)
val onErrorColor = Color(0xFFFFFFFF)
val backgroundColor = Color(0xFFF4F4FB)
val surfaceColor = Color(0xFFFFFFFF)
val textColor = Color(0xFF000000)

val darkPrimaryColor = Color(0xFF38BDFF)
val darkPrimaryVariantColor = Color(0xFF6371FF)
val darkOnPrimaryColor = Color(0xFFFFFFFF)
val darkSecondaryColor = Color(0xFF43444D)
val darkErrorColor = Color(0xFFF44336)
val darkOnErrorColor = Color(0xFFFFFFFF)
val darkBackgroundColor = Color(0xFF43444D)
val darkSurfaceColor = Color(0xFF000000)
val darkTextColor = Color(0xFFFFFFFF)

val LightColors = lightColorScheme(
    primary = primaryColor,
    onPrimary = onPrimaryColor,
    secondary = secondaryColor,
    onSecondary = textColor,
    background = backgroundColor,
    onBackground = textColor,
    surface = surfaceColor,
    onSurface = textColor,
    onError = onErrorColor,
    error = errorColor
)

val DarkColors = darkColorScheme(
    primary = darkPrimaryColor,
    onPrimary = darkOnPrimaryColor,
    secondary = darkSecondaryColor,
    onSecondary = darkTextColor,
    background = darkBackgroundColor,
    onBackground = darkTextColor,
    surface = darkSurfaceColor,
    onSurface = darkTextColor,
    onError = darkOnErrorColor,
    error = darkErrorColor
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
