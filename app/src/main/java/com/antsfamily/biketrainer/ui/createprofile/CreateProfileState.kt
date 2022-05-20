package com.antsfamily.biketrainer.ui.createprofile

sealed class CreateProfileState {
    object Loading : CreateProfileState()
    class TextFieldsState(
        val nameError: String?,
        val heightError: String?,
        val weightError: String?,
        val ageError: String?,
        ) : CreateProfileState()
    object Success : CreateProfileState()
    class NavigateToMain(val profileName: String) : CreateProfileState()
}
