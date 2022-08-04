package com.antsfamily.biketrainer.presentation.createworkout

import com.antsfamily.data.model.program.ProgramData

sealed class CreateWorkoutState {
    object Loading : CreateWorkoutState()
    data class TextFieldsState(
        val workoutNameError: String? = null,
        val workoutStepError: String? = null,
        val workoutDurationError: String? = null,
    ) : CreateWorkoutState()

    class Workouts(val steps: List<ProgramData>) : CreateWorkoutState()
}
