package com.antsfamily.biketrainer.ui.util

import android.view.MotionEvent
import com.antsfamily.domain.antservice.orZero
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import javax.inject.Inject

class BarChartGestureListener @Inject constructor() : OnChartGestureListener {

    private var barChart: BarChart? = null

    private var labels: List<String> = emptyList()

    private val isLabelsLessThanFive: Boolean
        get() = labels.size <= 5

    private val isChartScaledTwice: Boolean
        get() = barChart?.scaleX.orZero() > BarCharsStaticFields.FORMATTER_SCALE_X

    override fun onChartGestureStart(
        me: MotionEvent?, lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {}

    override fun onChartGestureEnd(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {
    }

    override fun onChartLongPressed(me: MotionEvent?) {}

    override fun onChartDoubleTapped(me: MotionEvent?) {}

    override fun onChartSingleTapped(me: MotionEvent?) {}

    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) {
    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
        barChart?.let {
            it.xAxis.apply {
                isEnabled = (isLabelsLessThanFive or isChartScaledTwice)
                setDefaultSettings(labels, it.getXAxisValueFormatter(labels))
            }
        }
    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {}

    fun setBarChart(barChart: BarChart) {
        this.barChart = barChart
    }

    fun setLabels(labels: List<String>) {
        this.labels = labels
    }
}
