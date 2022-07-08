package com.antsfamily.biketrainer.ui.createprofile

sealed class CreateProfileState {
    object Initial : CreateProfileState()
    object Loading : CreateProfileState()
    data class TextFieldsState(
        val nameError: String?,
        val heightError: String?,
        val weightError: String?,
        val ageError: String?,
    ) : CreateProfileState()
}
