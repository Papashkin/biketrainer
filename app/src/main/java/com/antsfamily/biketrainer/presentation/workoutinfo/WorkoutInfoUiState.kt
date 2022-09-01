package com.antsfamily.biketrainer.presentation.workoutinfo

import com.antsfamily.biketrainer.presentation.createprofile.model.LoadingState
import com.antsfamily.data.model.DeviceItem
import com.antsfamily.data.model.program.ProgramData

data class WorkoutInfoUiState(
    val loadingState: LoadingState = LoadingState.Nothing,
    val program: List<ProgramData> = emptyList(),
    val programName: String? = null,
    val sensors: List<DeviceItem> = emptyList(),
    val duration: String? = null,
    val maxPower: String? = null,
    val avgPower: String? = null
)
