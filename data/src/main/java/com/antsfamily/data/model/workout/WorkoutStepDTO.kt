package com.antsfamily.data.model.workout

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IndexedWorkoutStepDTO(
    val index: Int,
    val data: WorkoutStepDTO
)

@Serializable
sealed class WorkoutStepDTO {

    @Serializable
    @SerialName("one_step")
    data class OneStepDTO(val power: Int, val duration: Int) : WorkoutStepDTO()

    @Serializable
    @SerialName("power_ramp")
    data class RampStepDTO(val startPower: Int, val endPower: Int, val duration: Int) :
        WorkoutStepDTO()

    @Serializable
    @SerialName("intervals")
    data class IntervalsDTO(
        val power: Int,
        val powerDuration: Int,
        val rest: Int,
        val restDuration: Int,
        val reps: Int
    ) : WorkoutStepDTO()
}