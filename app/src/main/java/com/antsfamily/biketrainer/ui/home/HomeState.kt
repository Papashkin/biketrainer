package com.antsfamily.biketrainer.ui.home

sealed class HomeState {
    object Loading : HomeState()
    data class Content(val profileName: String) : HomeState()
}
