package com.antsfamily.biketrainer.ui.util

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.antsfamily.biketrainer.MainActivity

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    statusBarColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit
) {
    SetSystemBarColors(statusBarColor, darkTheme)
    MaterialTheme(
        colorScheme = if (darkTheme) darkColorScheme else lightColorScheme,
        typography = appTypography,
        content = content
    )
}

@Suppress("DEPRECATION")
@Composable
fun SetSystemBarColors(statusBarColor: Color, darkTheme: Boolean) {
    val context = LocalContext.current
    val window = (context as? Activity)?.window
    val view = LocalView.current

    LaunchedEffect(statusBarColor, darkTheme) {
        window?.let {
            it.statusBarColor = statusBarColor.toArgb()
            it.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(it, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }
}