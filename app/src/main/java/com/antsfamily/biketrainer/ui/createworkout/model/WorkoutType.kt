package com.antsfamily.biketrainer.ui.createworkout.model

import androidx.annotation.DrawableRes
import com.antsfamily.biketrainer.R

enum class WorkoutType {
    WarmUp,
    OneStep,
    Intervals,
    CoolDown,
    ;
}

@DrawableRes
fun WorkoutType.getIconId(): Int {
    return when (this) {
        WorkoutType.WarmUp -> R.drawable.ic_warm_up
        WorkoutType.OneStep -> R.drawable.ic_one_step
        WorkoutType.Intervals -> R.drawable.ic_intervals
        WorkoutType.CoolDown -> R.drawable.ic_cool_down
    }
}