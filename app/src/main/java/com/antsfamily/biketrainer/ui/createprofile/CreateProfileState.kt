package com.antsfamily.biketrainer.ui.createprofile

data class CreateProfileState(
    val isLoading: Boolean = false,
    val isCreateProfileButtonEnable: Boolean = false,
    val username: String? = null,
    val isUsernameErrorVisible: Boolean = false,
    val height: Int? = null,
    val isHeightErrorVisible: Boolean = false,
    val weight: Int? = null,
    val isWeightErrorVisible: Boolean = false,
    val age: Int? = null,
    val isAgeErrorVisible: Boolean = false,
)
