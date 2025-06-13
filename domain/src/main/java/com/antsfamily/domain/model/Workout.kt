package com.antsfamily.domain.model

data class Workout(
    val title: String,
    val data: List<IndexedWorkoutStep>,
)