package com.antsfamily.biketrainer.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.antsfamily.biketrainer.presentation.settings.SettingsUiState
import com.antsfamily.biketrainer.presentation.settings.SettingsViewModel
import com.antsfamily.biketrainer.ui.common.FullScreenLoading

interface SettingsScreen {
    companion object {
        @Composable
        fun Content() {
            SettingsScreen()
        }
    }
}

@Composable
private fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {

    val state = viewModel.state.collectAsState()

    when (val uiState = state.value) {
        is SettingsUiState.Loading -> FullScreenLoading()
        is SettingsUiState.Content -> SettingsContentScreen(
            state = uiState,
            onCircumferenceChanged = {
                viewModel.onCircumferenceChanged(it)
            },
            onThemeChanged = {
                viewModel.onThemeChanged(it)
            }
        )
        is SettingsUiState.Error -> TODO()
    }
}
