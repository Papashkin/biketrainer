package com.antsfamily.biketrainer.ui.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
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
val darkSecondaryColor = Color(0xFF1C1D25)
val darkErrorColor = Color(0xFFF44336)
val darkOnErrorColor = Color(0xFFFFFFFF)
val darkBackgroundColor = Color(0xFF1C1D25)
val darkSurfaceColor = Color(0xFF000000)
val darkTextColor = Color(0xFFFFFFFF)

val LightColors = lightColors(
    primary = primaryColor,
    primaryVariant = primaryVariantColor,
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

val DarkColors = darkColors(
    primary = darkPrimaryColor,
    primaryVariant = darkPrimaryVariantColor,
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
fun getThemeColors(): Colors = if (isSystemInDarkTheme()) DarkColors else LightColors
