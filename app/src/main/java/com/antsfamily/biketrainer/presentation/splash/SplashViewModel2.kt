package com.antsfamily.biketrainer.presentation.splash

import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.biketrainer.navigation.MainBottomItem
import com.antsfamily.biketrainer.navigation.Screen
import com.antsfamily.biketrainer.ui.splash.SplashScreenState
import com.antsfamily.biketrainer.ui.util.AppThemeSwitcher
import com.antsfamily.data.local.repositories.ProfilesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel2 @Inject constructor(
    private val profilesRepository: ProfilesRepository,
    private val themeSwitcher: AppThemeSwitcher
) : BaseViewModel2() {

    private val _uiState = MutableStateFlow<SplashScreenState>(SplashScreenState.Loading)
    val uiState: StateFlow<SplashScreenState> = _uiState

    private val _navigateToHomeFlow = MutableSharedFlow<Unit>()
    val navigateToHomeFlow: SharedFlow<Unit> = _navigateToHomeFlow.asSharedFlow()

    private val _navigateToCreateProfileFlow = MutableSharedFlow<Unit>()
    val navigateToCreateProfileFlow: SharedFlow<Unit> = _navigateToCreateProfileFlow.asSharedFlow()

    init {
        _uiState.value = SplashScreenState.Loading
        setAppTheme()
    }

    private fun setAppTheme() = viewModelScope.launch {
        val isDarkMode = profilesRepository.getDarkModeEnabled()
        themeSwitcher.setAppTheme(isDarkMode)
        delay(START_DELAY)
        getSelectedProfile()
    }

    private suspend fun getSelectedProfile() = withContext(Dispatchers.IO) {
        val profileName = profilesRepository.getSelectedProfileName()
        profileName?.let {
            _navigateToHomeFlow.emit(Unit)
        } ?: run {
            _navigateToCreateProfileFlow.emit(Unit)
        }
    }

    companion object {
        private const val START_DELAY = 200L
    }
}
