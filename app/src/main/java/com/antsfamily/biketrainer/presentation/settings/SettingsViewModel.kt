package com.antsfamily.biketrainer.presentation.settings

import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.biketrainer.core.model.Circumference
import com.antsfamily.biketrainer.ui.util.AppThemeSwitcher
import com.antsfamily.data.local.repositories.ProfilesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val profilesRepository: ProfilesRepository,
        private val themeSwitcher: AppThemeSwitcher
) : BaseViewModel2() {

    private val _state = MutableStateFlow<SettingsUiState>(SettingsUiState.Loading)
    val state: StateFlow<SettingsUiState>
        get() = _state

    init {
        getProfileData()
    }

    fun onCircumferenceChanged(circumference: Circumference) {
        _state.update {
            when (it) {
                is SettingsUiState.Content -> it.copy(wheelCircumference = circumference)
                else -> it
            }
        }
    }

    fun onThemeChanged(isDarkModeEnabled: Boolean) = viewModelScope.launch {
        profilesRepository.setDarkModeEnabled(isDarkModeEnabled)
        themeSwitcher.setAppTheme(isDarkModeEnabled)
        _state.update {
            when (it) {
                is SettingsUiState.Content -> it.copy(isDarkModeEnabled = isDarkModeEnabled)
                else -> it
            }
        }
    }

    private fun getProfileData() = viewModelScope.launch {
        try {
            val isDarkMode = profilesRepository.getDarkModeEnabled()
            val selectedProfileName = profilesRepository.getSelectedProfileName()
            selectedProfileName?.let { name ->
                _state.value = SettingsUiState.Content(
                    username = name,
                    isDarkModeEnabled = isDarkMode,
                    wheelCircumference = Circumference.UNKNOWN
                )
            } ?: run {
                _state.value = SettingsUiState.Error(SettingsErrorType.NO_PROFILES_FOUND)
            }
        } catch (e: Exception) {
            _state.value = SettingsUiState.Error(SettingsErrorType.UNKNOWN_ERROR)
        }
    }
}
