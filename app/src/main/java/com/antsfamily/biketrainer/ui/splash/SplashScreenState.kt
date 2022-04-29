package com.antsfamily.biketrainer.ui.splash

sealed class SplashScreenState {
    object Loading: SplashScreenState()
    class NavigateToMain(val profileName: String): SplashScreenState()
    object NavigateToCreateProfile: SplashScreenState()
}
