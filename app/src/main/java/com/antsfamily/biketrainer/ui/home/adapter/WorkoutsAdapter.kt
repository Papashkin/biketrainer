package com.antsfamily.biketrainer.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.databinding.CardWorkoutInfoBinding
import com.antsfamily.biketrainer.ui.util.hideAllLabels
import com.antsfamily.biketrainer.ui.util.setHighlightedMode
import com.antsfamily.biketrainer.util.fullTimeFormat
import com.antsfamily.data.model.program.Program
import com.antsfamily.data.model.program.ProgramData
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import javax.inject.Inject

class WorkoutsAdapter @Inject constructor() :
    ListAdapter<Program, WorkoutsAdapter.ProgramViewHolder>(WorkoutsDiffUtil()) {

    private var onItemClickListener: ((item: Program) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        val binding =
            CardWorkoutInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProgramViewHolder(binding)
    }

    fun setOnItemClickListener(listener: (item: Program) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProgramViewHolder(private val binding: CardWorkoutInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val emptyFormatter = object : ValueFormatter() {
            override fun getBarLabel(entry: BarEntry): String = ""
        }

        fun bind(item: Program) {
            val data = item.data
            with(binding) {
                programNameTv.text = item.title
                programDurationTv.text = getTotalTime(data)
                programMaxPowerTv.text = getMaxPower(data)
                programAvgPowerTv.text = getAveragePower(data)
                createChart(data)
                programBc.setOnClickListener { onItemClickListener?.invoke(item) }
                root.setOnClickListener { onItemClickListener?.invoke(item) }
            }
        }

        private fun getAveragePower(data: List<ProgramData>): String =
            data.sumOf { it.power }.div(data.size).toString()

        private fun getMaxPower(data: List<ProgramData>): String =
            data.maxOf { it.power }.toString()

        private fun getTotalTime(data: List<ProgramData>): String =
            data.sumOf { it.duration }.fullTimeFormat()

        private fun createChart(data: List<ProgramData>) {
            val entries = data.mapIndexed { index, _data ->
                BarEntry(index.toFloat(), _data.power.toFloat())
            }
            with(binding.programBc) {
                setScaleEnabled(false)
                setTouchEnabled(true)
                hideAllLabels()
                setDrawGridBackground(false)
                setDrawBorders(false)
                this.data = BarData(
                    BarDataSet(entries, "").apply {
                        barBorderWidth = BAR_BORDER_WIDTH
                        valueFormatter = emptyFormatter
                        color = R.color.color_central
                        stackLabels = emptyArray()
                        setHighlightedMode(true)
                    }
                ).apply { barWidth = BAR_WIDTH }
                animateXY(X_ANIMATION, Y_ANIMATION)
            }
        }
    }

    companion object {
        private const val BAR_BORDER_WIDTH = 0f
        private const val BAR_WIDTH = 1f
        private const val Y_ANIMATION = 900
        private const val X_ANIMATION = 700
    }
}
