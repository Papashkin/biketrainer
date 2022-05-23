package com.antsfamily.biketrainer.presentation.createprofile

import android.os.Handler
import android.os.Looper
import android.util.Log
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
        (_uiState.value as? CreateProfileState.TextFieldsState)?.let {
            _uiState.value = it.copy(nameError = null)
        }
    }

    fun onHeightChanged() {
        (_uiState.value as? CreateProfileState.TextFieldsState)?.let {
            _uiState.value = it.copy(heightError = null)
        }
    }

    fun onWeightChanged() {
        (_uiState.value as? CreateProfileState.TextFieldsState)?.let {
            _uiState.value = it.copy(weightError = null)
        }
    }

    fun onAgeChanged() {
        (_uiState.value as? CreateProfileState.TextFieldsState)?.let {
            _uiState.value = it.copy(ageError = null)
        }
    }

    fun createProfile(name: String?, height: Long?, weight: Long?, age: Int?) {
        if (isValid(name, height, weight, age)) {
            _uiState.value = CreateProfileState.Loading
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
            Handler(Looper.getMainLooper())
                .postDelayed({ _uiState.value = CreateProfileState.NavigateToMain(name) }, DELAY)
        } catch (e: Exception) {
            Log.e(this::class.java.name, e.message.orEmpty())
            //TODO add error handling stuff
        }
    }

    companion object {
        private const val DELAY = 500L
    }
}
