package com.antsfamily.biketrainer.ui.util

import android.content.Context
import androidx.annotation.DrawableRes
import com.antsfamily.biketrainer.R
import com.antsfamily.domain.model.WorkoutStep

@DrawableRes
fun WorkoutStep.getIconId(): Int {
    return when (this) {
        is WorkoutStep.CoolDown -> R.drawable.ic_cool_down
        is WorkoutStep.Intervals -> R.drawable.ic_intervals
        is WorkoutStep.OneStep -> R.drawable.ic_one_step
        is WorkoutStep.WarmUp -> R.drawable.ic_warm_up
    }
}

fun Context.getWorkoutSubtitle(step: WorkoutStep): String {
    return when (step) {
        is WorkoutStep.CoolDown -> getString(
            R.string.workout_step_subtitle_cooldown,
            step.formatDuration(step.duration)
        )

        is WorkoutStep.Intervals -> getString(
            R.string.workout_step_subtitle_intervals,
            step.formatDuration(step.restDuration),
            step.rest
        )

        is WorkoutStep.OneStep -> getString(
            R.string.workout_step_subtitle_onestep,
            step.formatDuration(step.duration)
        )

        is WorkoutStep.WarmUp -> getString(
            R.string.workout_step_subtitle_warmup,
            step.formatDuration(step.duration)
        )
    }
}

fun Context.getWorkoutTitle(step: WorkoutStep): String {
    return when (step) {
        is WorkoutStep.CoolDown -> getString(
            R.string.workout_step_title_cooldown,
            step.startPower,
            step.endPower
        )

        is WorkoutStep.Intervals -> getString(
            R.string.workout_step_title_intervals,
            step.reps,
            step.formatDuration(step.powerDuration),
            step.power
        )

        is WorkoutStep.OneStep -> getString(R.string.workout_step_title_onestep, step.power)
        is WorkoutStep.WarmUp -> getString(
            R.string.workout_step_title_warmup,
            step.startPower,
            step.endPower
        )
    }
}