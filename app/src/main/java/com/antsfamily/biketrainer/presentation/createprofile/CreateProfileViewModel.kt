package com.antsfamily.biketrainer.presentation.createprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.antsfamily.biketrainer.navigation.CreateProfileToHome
import com.antsfamily.biketrainer.presentation.Event
import com.antsfamily.biketrainer.presentation.StatefulViewModel
import com.antsfamily.biketrainer.util.orZero
import com.antsfamily.data.model.profile.Profile
import com.antsfamily.domain.Result
import com.antsfamily.domain.usecase.CreateProfileUseCase
import com.garmin.fit.Gender
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.math.BigDecimal

class CreateProfileViewModel @AssistedInject constructor(
    private val createProfileUseCase: CreateProfileUseCase
) : StatefulViewModel<CreateProfileViewModel.State>(State()) {

    @AssistedFactory
    interface Factory {
        fun build(): CreateProfileViewModel
    }

    data class State(
        val isLoading: Boolean = false,
        val genders: List<Gender> = Gender.values().toList(),
        val usernameError: String? = null,
        val ageError: String? = null,
        val weightError: String? = null,
        val heightError: String? = null,
        val genderError: String? = null
    )

    private val _clearFieldsEvent = MutableLiveData<Event<Unit>>()
    val clearFieldsEvent: LiveData<Event<Unit>>
        get() = _clearFieldsEvent

    private val _showSuccessAnimationEvent = MutableLiveData<Event<Unit>>()
    val showSuccessAnimationEvent: LiveData<Event<Unit>>
        get() = _showSuccessAnimationEvent

    private var userName: String? = null
    private var gender: Gender = Gender.INVALID

    fun onUsernameChange() {
        changeState { it.copy(usernameError = null) }
    }

    fun onAgeChange() {
        changeState { it.copy(ageError = null) }
    }

    fun onWeightChange() {
        changeState { it.copy(weightError = null) }
    }

    fun onHeightChange() {
        changeState { it.copy(heightError = null) }
    }

    fun onFemaleGenderSelected() {
        gender = Gender.FEMALE
        onGenderChange()
    }

    fun onMaleGenderSelected() {
        gender = Gender.MALE
        onGenderChange()
    }

    fun onCreateClick(
        username: String?,
        age: Int,
        weight: BigDecimal?,
        height: BigDecimal?
    ) {
        if (isValid(username, age, weight, height)) {
            showLoading()
            createProfile(
                username!!,
                age,
                weight.orZero(),
                height.orZero()
            )
        }
    }

    fun onAnimationEnd() {
        userName?.let {
            navigateTo(CreateProfileToHome(it))
        }
    }

//    fun navigateForward(profileName: String) {
//        navigateTo(CreateProfileToHome(profileName))
//    }

    private fun isValid(
        username: String?, age: Int, weight: BigDecimal?, height: BigDecimal?
    ): Boolean {
        val isUsernameValid = !username.isNullOrBlank()
        val isAgeValid = age in 1..109
        val isWeightValid = weight.orZero() > BigDecimal.ZERO
        val isHeightValid = height.orZero() > BigDecimal.ZERO
        val isGenderValid = gender != Gender.INVALID
        changeState {
            it.copy(
                usernameError = if (isUsernameValid) null else "This is required",
                ageError = if (isAgeValid) null else "This is required",
                weightError = if (isWeightValid) null else "This is required",
                heightError = if (isHeightValid) null else "This is required",
                genderError = if (isGenderValid) null else "This is required",
            )
        }
        return isUsernameValid && isAgeValid && isWeightValid && isHeightValid && isGenderValid
    }

    private fun createProfile(username: String, age: Int, weight: BigDecimal, height: BigDecimal) {
        createProfileUseCase(
            Profile(
                username,
                age,
                gender.toString(),
                weight.toFloat(),
                height.toFloat()
            ), ::handleResult
        )
    }

    private fun handleResult(result: Result<String, Error>) {
        when (result) {
            is Result.Success -> {
                userName = result.successData
                _showSuccessAnimationEvent.postValue(Event(Unit))
            }
            is Result.Failure -> {
                hideLoading()
                showErrorSnackbar(result.errorData.message ?: "Something went wrong :(")
            }
        }
    }

    private fun onGenderChange() {
        changeState { it.copy(genderError = null) }
    }

    private fun showLoading() {
        changeState { it.copy(isLoading = true) }
    }

    private fun hideLoading() {
        changeState { it.copy(isLoading = false) }
    }
}
