package com.antsfamily.biketrainer.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.data.local.repositories.ProfilesRepository
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel2 @AssistedInject constructor(
    private val profilesRepository: ProfilesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashScreenState>(SplashScreenState.Loading)
    val uiState: StateFlow<SplashScreenState> = _uiState

    init {
        getSelectedProfile()
    }

    private fun getSelectedProfile() = viewModelScope.launch {
        val profileName = profilesRepository.getSelectedProfileName()
        _uiState.value = profileName?.let {
            SplashScreenState.NavigateToMain(it)
        } ?: run {
            SplashScreenState.NavigateToCreateProfile
        }
    }
}
