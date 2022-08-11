package com.antsfamily.biketrainer.ui.createprofile

data class CreateProfileState(
    val isLoading: Boolean = false,
    val nameError: String? = null,
    val heightError: String? = null,
    val weightError: String? = null,
    val ageError: String? = null,
)
