package com.antsfamily.biketrainer.presentation.createprofile.model

sealed class LoadingState {
    object Nothing: LoadingState()
    object Loading : LoadingState()
    class Success(val fileName: String): LoadingState()
}
