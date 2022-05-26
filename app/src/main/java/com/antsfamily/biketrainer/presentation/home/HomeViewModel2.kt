package com.antsfamily.biketrainer.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.ui.home.HomeState
import com.antsfamily.data.local.repositories.ProfilesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel2 @Inject constructor(
    private val profilesRepository: ProfilesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeState>(HomeState.Loading)
    val uiState: StateFlow<HomeState> = _uiState

    init {
        getContent()
    }

    private fun getContent() = viewModelScope.launch {
        profilesRepository.getSelectedProfileName()?.let {
            _uiState.value = HomeState.Content(it)
        }
    }
}
