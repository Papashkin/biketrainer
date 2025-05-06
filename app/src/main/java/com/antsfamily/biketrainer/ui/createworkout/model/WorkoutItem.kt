package com.antsfamily.biketrainer.ui.createworkout.model

import com.antsfamily.biketrainer.util.fullTimeFormat
import com.antsfamily.biketrainer.util.timeFormat

data class WorkoutItem(
//    val entries: List<BarEntry>,
    val entries: List<String>,
    val labels: List<Long>
) {
    fun getLabelInTimeFormat() = labels.map { it.timeFormat() }
    fun getTotalTime() = labels.sum().fullTimeFormat()
}
