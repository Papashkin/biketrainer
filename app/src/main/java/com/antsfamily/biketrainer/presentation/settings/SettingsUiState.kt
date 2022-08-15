package com.antsfamily.biketrainer.presentation.settings

import com.antsfamily.data.model.Circumference

data class SettingsUiState(
    val isLoading: Boolean = false,
    val username: String,
    val age: Int,
    val wheelCircumference: Circumference = Circumference.UNKNOWN,
    val isDarkModeEnabled: Boolean = false,
) {
    companion object {
        fun empty() = SettingsUiState(false, "", 0)
    }
}
