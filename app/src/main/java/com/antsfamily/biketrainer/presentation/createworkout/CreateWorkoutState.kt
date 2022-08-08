package com.antsfamily.biketrainer.presentation.createworkout

import com.antsfamily.biketrainer.ui.createworkout.view.WorkoutType
import com.antsfamily.data.model.program.ProgramData

data class CreateWorkoutState(
    val isLoading: Boolean = false,
    val steps: List<ProgramData> = listOf(),
    val workoutType: WorkoutType = WorkoutType.STEP,
    val nameError: String? = null,
    val stepError: String? = null,
    val powerError: String? = null,
    val durationError: String? = null,
    val powerRestError: String? = null,
    val durationRestError: String? = null,
    val repeatsError: String? = null,
)
