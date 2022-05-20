package com.antsfamily.biketrainer.presentation.createprofile

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileState
import com.antsfamily.data.local.repositories.ProfilesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel2 @Inject constructor(
    private val profilesRepository: ProfilesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<CreateProfileState>(
        CreateProfileState.TextFieldsState(null, null, null, null)
    )
    val uiState: StateFlow<CreateProfileState> = _uiState

    fun onNameChanged() {
        val state = uiState.value
        updateTextFieldsState(
            nameError = null,
            heightError = (state as? CreateProfileState.TextFieldsState)?.heightError,
            weightError = (state as? CreateProfileState.TextFieldsState)?.weightError,
            ageError = (state as? CreateProfileState.TextFieldsState)?.ageError,
        )
    }

    fun onHeightChanged() {
        updateTextFieldsState(
            nameError = (uiState.value as? CreateProfileState.TextFieldsState)?.nameError,
            heightError = null,
            weightError = (uiState.value as? CreateProfileState.TextFieldsState)?.weightError,
            ageError = (uiState.value as? CreateProfileState.TextFieldsState)?.ageError,
        )
    }

    fun onWeightChanged() {
        updateTextFieldsState(
            nameError = (uiState.value as? CreateProfileState.TextFieldsState)?.nameError,
            heightError = (uiState.value as? CreateProfileState.TextFieldsState)?.heightError,
            weightError = null,
            ageError = (uiState.value as? CreateProfileState.TextFieldsState)?.ageError,
        )
    }

    fun onAgeChanged() {
        updateTextFieldsState(
            nameError = (uiState.value as? CreateProfileState.TextFieldsState)?.nameError,
            heightError = (uiState.value as? CreateProfileState.TextFieldsState)?.heightError,
            weightError = (uiState.value as? CreateProfileState.TextFieldsState)?.weightError,
            ageError = null,
        )
    }

    fun createProfile(name: String?, height: Long?, weight: Long?, age: Int?) {
        if (isValid(name, height, weight, age)) {
            saveProfile(name!!)
        }
    }

    private fun isValid(name: String?, height: Long?, weight: Long?, age: Int?): Boolean {
        val isAgeValid = age in 10..90
        val isNameValid = name.isNullOrBlank().not()
        val isHeightValid = (height ?: 0L) > 100L
        val isWeightValid = (weight ?: 0L) > 20L

        _uiState.value = CreateProfileState.TextFieldsState(
            nameError = if (isNameValid) null else "Username is invalid",
            heightError = if (isHeightValid) null else "Height is invalid",
            weightError = if (isWeightValid) null else "Weight is invalid",
            ageError = if (isAgeValid) null else "Age is invalid",
        )

        return isAgeValid && isNameValid && isHeightValid && isWeightValid
    }

    private fun saveProfile(name: String) = viewModelScope.launch {
        try {
            profilesRepository.setSelectedProfileName(name)
            _uiState.value = CreateProfileState.Loading
            Handler(Looper.getMainLooper())
                .postDelayed({ _uiState.value = CreateProfileState.NavigateToMain(name) }, DELAY)
        } catch (e: Exception) {
            //TODO add error handling stuff
        }
    }

    private fun updateTextFieldsState(
        nameError: String?,
        heightError: String?,
        weightError: String?,
        ageError: String?,
    ) {
        _uiState.value = CreateProfileState.TextFieldsState(
            nameError = nameError,
            heightError = heightError,
            weightError = weightError,
            ageError = ageError,
        )
    }

    companion object {
        private const val DELAY = 500L
    }
}
