package com.antsfamily.biketrainer.ui.common.workoutchart

import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import com.antsfamily.biketrainer.ui.util.Padding
import com.antsfamily.data.model.program.ProgramData
import com.antsfamily.data.model.program.getPowerMax
import com.antsfamily.data.model.program.getTotalDuration
import java.text.SimpleDateFormat
import java.util.*

class WorkoutChartState {
    private var workoutSteps = listOf<ProgramData>()
    private var stepSizeCoef = 1
    private var isLabelReadable = true

    fun getPowerCoef(height: Float): Float {
        val relation = workoutSteps.getPowerMax() / height
        return if (relation >= 2f) relation.div(2) else 1f
    }

    fun getDurationCoef(width: Float): Float {
        val coefficient = width / workoutSteps.getTotalDuration()
        isLabelReadable = (workoutSteps.size < 15 || coefficient < Padding.large.value)
        return coefficient
    }

    val powerLines: List<Int>
        get() = mutableListOf<Int>().apply {
            repeat(POWER_LINES_COUNT) {
                if (it > 0) add(POWER_LINE_STEP * it)
            }
        }

    val durationLines: List<Pair<Int, String>>
        get() = mutableListOf<Pair<Int, String>>().apply {
            if (workoutSteps.getTotalDuration() > (DURATION_LABEL_CHECK_VALUE * stepSizeCoef)) {
                stepSizeCoef++
            }
            val stepSize = DURATION_LINE_STEP * stepSizeCoef

            val durationLineCount = workoutSteps.getTotalDuration() / stepSize

            repeat(durationLineCount.toInt().plus(1)) {
                val timeline = stepSize * it
                if (it > 0 && workoutSteps.isNotEmpty()) add(
                    Pair(
                        timeline,
                        getFormattedDuration(timeline)
                    )
                )
            }
        }

    fun isLabelReadable(): Boolean {
        return isLabelReadable
    }

    fun update(workoutSteps: List<ProgramData>) {
        this.workoutSteps = workoutSteps
    }

    private fun getFormattedDuration(duration: Int): String {
        val date = Date(duration * 1000L)
        val dateFormatter = SimpleDateFormat("H:mm:ss").apply {
            timeZone = TimeZone.getTimeZone("GMT")
        }
        return dateFormatter.format(date)
    }

    companion object {
        private const val POWER_LINES_COUNT = 20
        private const val POWER_LINE_STEP = 100
        private const val DURATION_LINE_STEP = 300
        private const val DURATION_LABEL_CHECK_VALUE = 1800

        fun getState(workoutSteps: List<ProgramData>) = WorkoutChartState().apply {
            this.workoutSteps = workoutSteps
        }

        @Suppress("UNCHECKED_CAST")
        val Saver: Saver<WorkoutChartState, Any> = listSaver(
            save = { listOf(it.workoutSteps) },
            restore = {
                getState(workoutSteps = it.firstOrNull().orEmpty())
            }
        )
    }
}
