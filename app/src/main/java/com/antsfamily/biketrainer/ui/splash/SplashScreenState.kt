package com.antsfamily.biketrainer.ui.splash

sealed class SplashScreenState {
    object Loading: SplashScreenState()
    object NavigateToMain: SplashScreenState()
    object NavigateToCreateProfile: SplashScreenState()
}
