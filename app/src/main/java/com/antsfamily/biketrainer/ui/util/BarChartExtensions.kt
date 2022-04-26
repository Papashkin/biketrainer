package com.antsfamily.biketrainer.ui.util

import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.ui.util.BarCharsStaticFields.BAR_BORDER_WIDTH
import com.antsfamily.biketrainer.ui.util.BarCharsStaticFields.BAR_GRANULARITY
import com.antsfamily.biketrainer.ui.util.BarCharsStaticFields.FORMATTER_SCALE_X
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter

fun BarChart.getYAxisValueFormatter(entrySize: Int) = object : ValueFormatter() {
    private val isLabelsLessThanFive: Boolean
        get() = entrySize <= 5

    private val isChartScaledTwice: Boolean
        get() = this@getYAxisValueFormatter.scaleX > FORMATTER_SCALE_X

    override fun getBarLabel(entry: BarEntry): String {
        return if (isLabelsLessThanFive or isChartScaledTwice) entry.y.toString() else ""
    }
}

fun BarChart.getXAxisValueFormatter(labels: List<String>) = object : ValueFormatter() {
    private val isLabelsLessThanFive: Boolean
        get() = labels.size <= 5

    private val isChartScaledTwice: Boolean
        get() = this@getXAxisValueFormatter.scaleX > FORMATTER_SCALE_X

    override fun getFormattedValue(value: Float): String =
        if (isLabelsLessThanFive or isChartScaledTwice) {
            labels.getOrNull(value.toInt()).orEmpty()
        } else {
            ""
        }
}

fun BarChart.setDefaultBaseSettings(entryCount: Int) = this.apply {
    isScaleYEnabled = false
    isScaleXEnabled = entryCount > 5
    setTouchEnabled(true)
    description.isEnabled = false
    legend.isEnabled = false
    axisLeft.isEnabled = true
    axisRight.isEnabled = false
    setDrawGridBackground(false)
    setDrawBorders(false)
}

fun XAxis.setDefaultSettings(labels: List<String>, formatter: ValueFormatter) = this.apply {
    position = XAxis.XAxisPosition.BOTTOM
    setDrawGridLines(false)
    granularity = BAR_GRANULARITY
    labelCount = labels.size
    valueFormatter = formatter
}

fun BarDataSet.setDefaultSettings(formatter: ValueFormatter) = this.apply {
    barBorderWidth = BAR_BORDER_WIDTH
    valueTextSize = 11f
    valueFormatter = formatter
    color = R.color.color_central
    isHighlightEnabled = false
}

fun BarChart.setHighlightedMode(isFull: Boolean) {
    this.apply {
        isHighlightPerTapEnabled = false
        isHighlightFullBarEnabled = false
        isHighlightPerDragEnabled = false
        data?.dataSets?.forEach {
            it.isHighlightEnabled = !isFull
        }
    }
}

fun BarChart.hideAllLabels() {
    this.apply {
        description.isEnabled = false
        legend.isEnabled = false
        xAxis.isEnabled = false
        axisLeft.isEnabled = false
        axisRight.isEnabled = false
    }
}

object BarCharsStaticFields {
    const val FORMATTER_SCALE_X = 2.4f
    const val BAR_GRANULARITY = 1f
    const val BAR_BORDER_WIDTH = 0f
    const val BAR_WIDTH_95 = 0.95f
}
