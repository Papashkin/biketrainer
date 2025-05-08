package com.antsfamily.biketrainer.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.ui.util.AppThemeSwitcher
import com.antsfamily.data.local.repositories.ProfilesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    profilesRepository: ProfilesRepository,
    private val themeSwitcher: AppThemeSwitcher,
) : ViewModel() {

    private val _state = MutableStateFlow(profilesRepository.getDarkModeEnabled())
    val state: StateFlow<Boolean> = _state

    init {
        subscribeToDarkThemeChange()
    }

    private fun subscribeToDarkThemeChange() = viewModelScope.launch {
        themeSwitcher.darkThemeState.collectLatest { isDarkTheme ->
            _state.value = isDarkTheme
        }
    }
}
