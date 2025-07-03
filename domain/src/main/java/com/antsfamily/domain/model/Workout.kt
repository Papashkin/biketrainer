package com.antsfamily.domain.model

data class Workout(
    val id: Int,
    val title: String,
    val data: List<IndexedWorkoutStep>,
) {
    val averagePower: Int
        get() {
            return data.sumOf { it.step.averagePower } / data.size
        }

    val maxPower: Int
        get() {
            return data.maxOf { it.step.maxPower }
        }

    val totalDuration: Long
        get() {
            val allDurations = data.sumOf { it.step.getTotalDuration() }.toLong()
            return allDurations
        }
}