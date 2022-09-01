package com.antsfamily.biketrainer.presentation.main

import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.biketrainer.ui.util.AppThemeSwitcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val themeSwitcher: AppThemeSwitcher,
) : BaseViewModel2() {

    private val _state = MutableStateFlow(MainUiState(themeSwitcher.darkThemeState.value))
    val state: StateFlow<MainUiState> = _state

    init {
        initDarkThemeChange()
    }

    private fun initDarkThemeChange() = viewModelScope.launch {
        themeSwitcher.darkTheme.collectLatest { isDarkTheme ->
            _state.update {
                it.copy(isDarkTheme = isDarkTheme)
            }
        }
    }
}
