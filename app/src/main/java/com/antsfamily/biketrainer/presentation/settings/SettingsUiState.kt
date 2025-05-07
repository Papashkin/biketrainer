package com.antsfamily.biketrainer.presentation.settings

import com.antsfamily.biketrainer.core.model.Circumference

sealed class SettingsUiState {
    data object Loading: SettingsUiState()
    data class Content(
        val username: String,
        val wheelCircumference: Circumference,
        val isDarkModeEnabled: Boolean
    ): SettingsUiState()
    data class Error(val type: SettingsErrorType): SettingsUiState()
}

enum class SettingsErrorType {
    NO_PROFILE_SELECTED,
    NO_PROFILES_FOUND,
    UNKNOWN_ERROR,
    ;
}