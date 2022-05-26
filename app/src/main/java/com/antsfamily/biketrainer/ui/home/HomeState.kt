package com.antsfamily.biketrainer.ui.home

import com.antsfamily.data.model.program.Program

sealed class HomeState {
    object Loading : HomeState()
    data class ContentWithData(
        val profileName: String,
        val workouts: List<Program>,
    ) : HomeState()
    data class EmptyContent(val profileName: String) : HomeState()
}
