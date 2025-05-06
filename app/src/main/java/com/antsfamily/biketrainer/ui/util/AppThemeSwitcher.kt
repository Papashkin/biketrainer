package com.antsfamily.biketrainer.ui.util

import com.antsfamily.data.local.repositories.ProfilesRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppThemeSwitcher @Inject constructor(profilesRepository: ProfilesRepository) {

    private val _darkThemeState = MutableStateFlow(profilesRepository.getDarkModeEnabled())
    val darkThemeState: StateFlow<Boolean> = _darkThemeState

    val darkTheme = MutableSharedFlow<Boolean>()

    suspend fun setAppTheme(isDark: Boolean) {
        darkTheme.emit(isDark)
    }
}
