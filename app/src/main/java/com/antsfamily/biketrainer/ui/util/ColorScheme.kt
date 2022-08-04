package com.antsfamily.biketrainer.ui.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//val primaryColor = Color(0xFF265C4B)
//val onPrimaryColor = Color(0xFFFFFFFF)
//val secondaryColor = Color(0xFF8B635C)
//val errorColor = Color(0xFFF44336)
//val onErrorColor = Color(0xFFFFFFFF)
//val backgroundColor = Color(0xFFF0EBE2)
//val surfaceColor = Color(0xFFF7F5ED)
//val textColor = Color(0xFF000000)

val primaryColor = Color(0xFF38BDFF)
val primaryVariantColor = Color(0xFF6371FF)
val onPrimaryColor = Color(0xFFFFFFFF)
val secondaryColor = Color(0xFFF4F4FB)
val errorColor = Color(0xFFF44336)
val onErrorColor = Color(0xFFFFFFFF)
val backgroundColor = Color(0xFFF4F4FB)
val surfaceColor = Color(0xFFFFFFFF)
val textColor = Color(0xFF000000)

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

@Composable
fun getThemeColors(): Colors = if (isSystemInDarkTheme()) DarkColors else LightColors
