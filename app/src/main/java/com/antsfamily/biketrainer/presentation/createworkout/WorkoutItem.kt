package com.antsfamily.biketrainer.presentation.createworkout

import java.io.Serializable

data class WorkoutItem(
    val power: Int = 0,
    val duration: String = "",
    val powerRest: Int = 0,
    val durationRest: String = "",
    val repeats: Int = 0
) : Serializable
