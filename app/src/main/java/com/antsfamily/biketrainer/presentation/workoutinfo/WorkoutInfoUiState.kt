package com.antsfamily.biketrainer.presentation.workoutinfo

import com.antsfamily.biketrainer.presentation.createprofile.model.LoadingState
import com.antsfamily.domain.model.DeviceItem
import com.antsfamily.domain.model.Workout

data class WorkoutInfoUiState(
    val loadingState: LoadingState = LoadingState.Nothing,
    val workout: Workout? = null,
    val name: String? = null,
    val sensors: List<DeviceItem> = emptyList(),
    val duration: String? = null,
    val maxPower: String? = null,
    val avgPower: String? = null
)
