package com.antsfamily.biketrainer.presentation.createprofile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileState
import com.antsfamily.domain.model.Profile
import com.antsfamily.domain.antservice.orZero
import com.antsfamily.domain.usecase.profile.CreateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val createProfileUseCase: CreateProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CreateProfileState())
    val state: StateFlow<CreateProfileState> = _state

    private val _navigateToHomeEvent = MutableSharedFlow<Unit>()
    val navigateToHomeEvent: SharedFlow<Unit> = _navigateToHomeEvent.asSharedFlow()

    fun onNameChanged(value: String?) {
        _state.update {
            it.copy(
                username = value,
                isUsernameErrorVisible = value.isNullOrBlank(),
            )
        }
        validateState()
    }

    fun onHeightChanged(value: String?) {
        val height = value?.toIntOrNull()
        _state.update {
            it.copy(
                height = height,
                isHeightErrorVisible = height.orZero() <= 50,
            )
        }
        validateState()
    }

    fun onWeightChanged(value: String?) {
        val weight = value?.toIntOrNull()
        _state.update {
            it.copy(
                weight = weight,
                isWeightErrorVisible = weight.orZero() <= 10,
            )
        }
        validateState()
    }

    fun onAgeChanged(value: String?) {
        val age = value?.toIntOrNull()
        _state.update {
            it.copy(
                age = age,
                isAgeErrorVisible = age.orZero() < 12,
            )
        }
        validateState()
    }

    private fun validateState() {
        _state.update {
            it.copy(isCreateProfileButtonEnable = isDataValid())
        }
    }

    fun onProfileCreateClick() = viewModelScope.launch {
        _state.update {
            it.copy(isLoading = true)
        }
        saveProfile()
    }

    private fun isDataValid(): Boolean {
        val uiState = _state.value
        if (uiState.username.isNullOrBlank() || uiState.isUsernameErrorVisible) return false
        if (uiState.height == null || uiState.isHeightErrorVisible) return false
        if (uiState.weight == null || uiState.isWeightErrorVisible) return false
        if (uiState.age == null || uiState.isAgeErrorVisible) return false

        return true
    }

    private suspend fun saveProfile() {
        try {
            val profile = Profile(
                name = state.value.username!!.trim(),
                age = state.value.age!!,
                weight = state.value.weight?.toFloat()!!,
                height = state.value.height?.toFloat()!!
            )
            createProfileUseCase(profile)
            _navigateToHomeEvent.emit(Unit)
        } catch (e: Exception) {
            Log.e(this::class.java.name, e.message.orEmpty())
            //TODO add error handling stuff
        }
    }
}
