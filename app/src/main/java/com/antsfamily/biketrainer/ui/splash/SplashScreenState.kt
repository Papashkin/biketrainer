package com.antsfamily.biketrainer.ui.splash

sealed class SplashScreenState {
    data class Loading(val isDarkTheme: Boolean = false): SplashScreenState()
}
