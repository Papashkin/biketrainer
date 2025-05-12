package com.antsfamily.biketrainer.ui.createworkout.model

import android.content.Context
import androidx.annotation.DrawableRes
import com.antsfamily.biketrainer.R

data class IndexedWorkoutStep(val index: Int, val step: WorkoutStep)

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

fun WorkoutStep.toCardTitleStringRes(context: Context): String {
    return when (this) {
        is WorkoutStep.CoolDown -> context.getString(
            R.string.workout_step_title_cooldown,
            startPower,
            endPower
        )

        is WorkoutStep.Intervals -> context.getString(
            R.string.workout_step_title_intervals,
            reps,
            this.formatDuration(powerDuration),
            power
        )

        is WorkoutStep.OneStep -> context.getString(R.string.workout_step_title_onestep, power)
        is WorkoutStep.WarmUp -> context.getString(
            R.string.workout_step_title_warmup,
            startPower,
            endPower
        )
    }
}

fun WorkoutStep.toCardSubtitleStringRes(context: Context): String {
    return when (this) {
        is WorkoutStep.CoolDown -> context.getString(
            R.string.workout_step_subtitle_cooldown,
            this.formatDuration(duration)
        )

        is WorkoutStep.Intervals -> context.getString(
            R.string.workout_step_subtitle_intervals,
            this.formatDuration(restDuration),
            rest
        )

        is WorkoutStep.OneStep -> context.getString(
            R.string.workout_step_subtitle_onestep,
            this.formatDuration(duration)
        )

        is WorkoutStep.WarmUp -> context.getString(
            R.string.workout_step_subtitle_warmup,
            this.formatDuration(duration)
        )
    }
}

@DrawableRes
fun WorkoutStep.getIconId(): Int {
    return when (this) {
        is WorkoutStep.CoolDown -> R.drawable.ic_cool_down
        is WorkoutStep.Intervals -> R.drawable.ic_intervals
        is WorkoutStep.OneStep -> R.drawable.ic_one_step
        is WorkoutStep.WarmUp -> R.drawable.ic_warm_up
    }
}