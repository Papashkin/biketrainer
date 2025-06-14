package com.antsfamily.domain.model

data class Workout(
    val title: String,
    val data: List<IndexedWorkoutStep>,
) {
    val averagePower: Int
        get() {
            return data.sumOf { it.step.averagePower } / data.size
        }

    val totalDuration: Long
        get() {
            val allDurations = data.sumOf { it.step.getTotalDuration() }.toLong()
            return allDurations
        }
}