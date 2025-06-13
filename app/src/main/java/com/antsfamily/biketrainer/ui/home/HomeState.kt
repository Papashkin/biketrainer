package com.antsfamily.biketrainer.ui.home

import com.antsfamily.domain.model.Workout

sealed class HomeState {
    object Loading : HomeState()
    data class ContentWithData(val profileName: String, val workouts: List<Workout>) : HomeState()
    data class EmptyContent(val profileName: String) : HomeState()
}
