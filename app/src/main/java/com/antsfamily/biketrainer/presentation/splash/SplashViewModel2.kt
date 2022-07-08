package com.antsfamily.biketrainer.presentation.splash

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.biketrainer.navigation.MainBottomItem
import com.antsfamily.biketrainer.navigation.Screen
import com.antsfamily.biketrainer.ui.splash.SplashScreenState
import com.antsfamily.data.local.repositories.ProfilesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel2 @Inject constructor(
    private val profilesRepository: ProfilesRepository,
) : BaseViewModel2() {

    private val _uiState = MutableStateFlow<SplashScreenState>(SplashScreenState.Loading)
    val uiState: StateFlow<SplashScreenState> = _uiState

    init {
        _uiState.value = SplashScreenState.Loading
        Handler(Looper.getMainLooper()).postDelayed(::getSelectedProfile, START_DELAY)
    }

    private fun getSelectedProfile() = viewModelScope.launch {
        val profileName = profilesRepository.getSelectedProfileName()
        profileName?.let {
            navigateTo(MainBottomItem.Home)
        } ?: run {
            navigateTo(Screen.CreateProfile)
        }
    }

    companion object {
        private const val START_DELAY = 500L
    }
}
