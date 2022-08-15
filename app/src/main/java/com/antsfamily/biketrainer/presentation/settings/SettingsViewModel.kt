package com.antsfamily.biketrainer.presentation.settings

import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.biketrainer.ui.util.AppThemeSwitcher
import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.data.model.Circumference
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

    private val _state = MutableStateFlow(SettingsUiState.empty())
    val state: StateFlow<SettingsUiState>
        get() = _state

    init {
        getProfileData()
    }

    fun isCircumferenceClicked(circumference: Circumference) {
        _state.update {
            it.copy(wheelCircumference = circumference)
        }
    }

    fun onUiModeChanged(isDarkModeEnabled: Boolean) = viewModelScope.launch {
        profilesRepository.setDarkModeEnabled(isDarkModeEnabled)
        themeSwitcher.setAppTheme(isDarkModeEnabled)
        _state.update {
            it.copy(isDarkModeEnabled = isDarkModeEnabled)
        }
    }

    private fun getProfileData() = viewModelScope.launch {
        val isDarkMode = profilesRepository.getDarkModeEnabled()
        profilesRepository.getSelectedProfileName()?.let { name ->
            _state.update {
                it.copy(username = name, isDarkModeEnabled = isDarkMode)
            }
        }
    }
}
