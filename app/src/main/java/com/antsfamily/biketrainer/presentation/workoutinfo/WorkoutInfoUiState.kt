package com.antsfamily.biketrainer.presentation.workoutinfo

import com.antsfamily.domain.model.DeviceItem
import com.antsfamily.domain.model.Workout

sealed class WorkoutInfoUiState {

    data object Loading : WorkoutInfoUiState()

    data class Content(
        val workout: Workout,
        val sensors: List<DeviceItem> = emptyList(),
    ) : WorkoutInfoUiState()
}
