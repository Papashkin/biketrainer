package com.antsfamily.biketrainer.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.ui.splash.SplashScreenState
import com.antsfamily.domain.repository.ProfilesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val profilesRepository: ProfilesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashScreenState>(SplashScreenState.Loading())
    val uiState: StateFlow<SplashScreenState> = _uiState

    private val _navigateToHomeFlow = MutableSharedFlow<Unit>()
    val navigateToHomeFlow: SharedFlow<Unit> = _navigateToHomeFlow.asSharedFlow()

    private val _navigateToCreateProfileFlow = MutableSharedFlow<Unit>()
    val navigateToCreateProfileFlow: SharedFlow<Unit> = _navigateToCreateProfileFlow.asSharedFlow()

    private val _navigateToSelectProfileFlow = MutableSharedFlow<Unit>()
    val navigateToSelectProfileFlow: SharedFlow<Unit> = _navigateToSelectProfileFlow.asSharedFlow()

    init {
        setAppTheme()
    }

    private fun setAppTheme() = viewModelScope.launch {
        val isDarkMode = profilesRepository.getDarkModeEnabled()
        _uiState.value = SplashScreenState.Loading(isDarkMode)
        getProfiles()
    }

    private suspend fun getProfiles() {
        val profiles = profilesRepository.getAllProfiles()
        when {
            profiles.isEmpty() -> _navigateToCreateProfileFlow.emit(Unit)
            profiles.size == 1 -> _navigateToHomeFlow.emit(Unit)
            else -> {
                _navigateToSelectProfileFlow.emit(Unit)
                //TODO Handle multiple profiles if needed (add new screen with profile selection)
            }
        }
    }
}
