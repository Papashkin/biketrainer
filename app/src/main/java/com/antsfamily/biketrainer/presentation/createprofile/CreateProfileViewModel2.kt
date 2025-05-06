package com.antsfamily.biketrainer.presentation.createprofile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.antsfamily.biketrainer.BaseViewModel2
import com.antsfamily.biketrainer.navigation.MainBottomItem
import com.antsfamily.biketrainer.ui.createprofile.CreateProfileState
import com.antsfamily.data.local.repositories.ProfilesRepository
import com.antsfamily.domain.antservice.orZero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel2 @Inject constructor(
    private val repository: ProfilesRepository,
) : BaseViewModel2() {

    private val _uiState = MutableStateFlow(CreateProfileState())
    val uiState: StateFlow<CreateProfileState> = _uiState

    fun onNameChanged() {
        _uiState.update {
            it.copy(nameError = null)
        }
    }

    fun onHeightChanged() {
        _uiState.update {
            it.copy(heightError = null)
        }
    }

    fun onWeightChanged() {
        _uiState.update {
            it.copy(weightError = null)
        }
    }

    fun onAgeChanged() {
        _uiState.update {
            it.copy(ageError = null)
        }
    }

    fun onProfileCreateClick(name: String?, height: Int?, weight: Int?, age: Int?) {
        if (isValid(name, height, weight, age)) {
            _uiState.update {
                it.copy(isLoading = true)
            }
            saveProfile(name!!.trim())
        }
    }

    private fun isValid(name: String?, height: Int?, weight: Int?, age: Int?): Boolean {
        val isAgeValid = age in 10..90
        val isNameValid = name.isNullOrBlank().not()
        val isHeightValid = (height.orZero()) > 100
        val isWeightValid = (weight.orZero()) > 20

        _uiState.update {
            it.copy(
                nameError = if (isNameValid) null else "Username is invalid",
                heightError = if (isHeightValid) null else "Height is invalid",
                weightError = if (isWeightValid) null else "Weight is invalid",
                ageError = if (isAgeValid) null else "Age is invalid",
            )
        }

        return isAgeValid && isNameValid && isHeightValid && isWeightValid
    }

    private fun saveProfile(name: String) = viewModelScope.launch {
        try {
            repository.setSelectedProfileName(name)
            navigateTo(MainBottomItem.Home)
        } catch (e: Exception) {
            Log.e(this::class.java.name, e.message.orEmpty())
            //TODO add error handling stuff
        }
    }
}
