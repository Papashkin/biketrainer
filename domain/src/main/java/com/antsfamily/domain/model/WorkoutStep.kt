package com.antsfamily.domain.model

sealed class WorkoutStep {

    data class OneStep(val power: Int, val duration: Duration) : WorkoutStep()
    data class Intervals(
        val power: Int,
        val powerDuration: Duration,
        val rest: Int,
        val restDuration: Duration,
        val reps: Int
    ) : WorkoutStep()

    data class WarmUp(val startPower: Int, val endPower: Int, val duration: Duration) :
        WorkoutStep()

    data class CoolDown(val startPower: Int, val endPower: Int, val duration: Duration) :
        WorkoutStep()

    fun formatDuration(duration: Duration): String {
        if (duration.isZero) return ""
        if (duration.minutes == 0) {
            return "${duration.seconds} sec."
        }
        if (duration.seconds == 0) {
            return "${duration.minutes} min."
        }

        return "${duration.minutes} min. ${duration.seconds} sec."
    }

    fun getTotalDuration(): Int = when (this) {
        is CoolDown -> this.duration.total
        is Intervals -> this.powerDuration.total.plus(this.restDuration.total).times(this.reps)
        is OneStep -> this.duration.total
        is WarmUp -> this.duration.total
    }
}