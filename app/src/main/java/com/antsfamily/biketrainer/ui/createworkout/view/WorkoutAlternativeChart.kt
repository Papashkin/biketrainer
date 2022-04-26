package com.antsfamily.biketrainer.ui.createworkout.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.ViewWorkoutChartAlternativeBinding
import com.antsfamily.biketrainer.util.fullTimeFormat
import com.antsfamily.data.model.program.ProgramData
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

class WorkoutAlternativeChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: ViewWorkoutChartAlternativeBinding =
        ViewWorkoutChartAlternativeBinding.inflate(LayoutInflater.from(context), this, true)

    var item: List<ProgramData> = emptyList()
        set(value) {
            field = value
            setupBarChartData(value)
        }

    var isEmptyDataVisible: Boolean = false
        set(value) {
            field = value
            binding.emptyDataFl.isVisible = value
        }

    var isChartVisible: Boolean = false
        set(value) {
            field = value
            binding.workoutChart.isVisible = value
        }

    var error: String? = null
        set(value) {
            field = value
            setupError(value)
        }

    private fun setupError(error: String?) {
        binding.errorTv.text = error
        binding.errorTv.isVisible = error != null
    }

    init {
        setupChart()
    }

    private fun setupChart() {
        with(binding.workoutChart) {
            setScaleEnabled(false)
            setTouchEnabled(true)
            description = null
            getAxis(YAxis.AxisDependency.LEFT).axisMinimum = 0f
            xAxis.axisMinimum = 0f
            xAxis.setCenterAxisLabels(false)
            xAxis.isEnabled = false
            axisRight.isEnabled = false
            legend.isEnabled = false
        }
    }

    private fun setupBarChartData(items: List<ProgramData>) {
        with(binding) {
            emptyDataFl.isVisible = items.isEmpty()
            workoutChart.isVisible = items.isNotEmpty()

            val dataSets = getLineData(items)

            workoutChart.apply {
                data = LineData(dataSets).apply {
                    disableScroll()
                }
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getBarLabel(entry: BarEntry): String = "-"
                }
            }
            workoutLegendTv.text = if (items.isEmpty()) {
                ""
            } else {
                "Total time: ${items.sumOf { it.duration}.fullTimeFormat()}"
            }
            workoutChart.invalidate()
        }
    }

    private fun getLineData(items: List<ProgramData>): List<ILineDataSet> {
        /*
        The powers were just randomly generated numbers for my purpose.
        And the durations just need to increment each time and durations should always have one more entry than powers.
        A sample entry could be: durations = [0, 12, 15, 19, 33] powers = [20, 9, 12, 16]
        In our purposes:
        items - a list of ProgramData (contains power and duration)
         */
        val dataSets: MutableList<ILineDataSet> = mutableListOf()
        val barPoints: MutableList<Entry> = mutableListOf()
        var durationFromStart = 0f

        val powers = items.map { it.power }
        val durations = items.map { it.duration }

        var dataSet: LineDataSet? = null
        powers.forEachIndexed { index, power ->
            if (power >= 0) {
                val barPoint1 = Entry(durationFromStart, power.toFloat())
                durationFromStart += durations[index].toFloat()
                val barPoint2 = Entry(durationFromStart, power.toFloat())
                barPoints.add(barPoint1)
                barPoints.add(barPoint2)
                dataSet = LineDataSet(barPoints, "$power").apply {
                    color = R.color.color_overside
                    fillColor = R.color.color_overside
                }
            }
        }
        dataSet?.setDrawCircles(false)
        dataSet?.setDrawFilled(true)
        dataSet?.setDrawValues(false)
        dataSet?.let {
            dataSets.add(it)
        }

        return dataSets
    }
}
