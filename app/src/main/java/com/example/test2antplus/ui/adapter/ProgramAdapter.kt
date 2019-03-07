package com.example.test2antplus.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.test2antplus.Program
import com.example.test2antplus.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class ProgramAdapter: RecyclerView.Adapter<ProgramAdapter.ProgramViewHolder>() {
    private var programs: ArrayList<Program> = arrayListOf()
    private lateinit var selectedProgram: Program
    private lateinit var programsDiffUtil: ProgramCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramAdapter.ProgramViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_program_info, parent, false)
        return ProgramViewHolder(view)
    }

    fun addDevice(newProgram: Program) {
        val oldPrograms = this.getData()
        if (!programs.contains(newProgram)) {
            programs.add(newProgram)
        }
        programsDiffUtil = ProgramCallback(oldPrograms, programs)
        val productDiffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(programsDiffUtil, false)
        productDiffResult.dispatchUpdatesTo(this)
        this.notifyItemInserted(programs.size)
    }

    fun getSelectedPrograms(): Program = selectedProgram

    fun getAllData() = programs

    private fun getData(): ArrayList<Program> = programs

    override fun getItemCount(): Int = programs.size

    override fun onBindViewHolder(holder: ProgramAdapter.ProgramViewHolder, position: Int) {
        holder.bind(this.programs[position], position)
    }

    inner class ProgramViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val programName = view.findViewById<TextView>(R.id.textProgramName)
        private val avgPower = view.findViewById<TextView>(R.id.textAveragePower)
        private val programChart = view.findViewById<LineChart>(R.id.chartProgram)
        private val checkBox = view.findViewById<CheckBox>(R.id.checkBoxIsSelected)

        fun bind(program: Program, position: Int) {

            programName.text = program.getName()

            avgPower.text = getAveragePower(program.getProgram())

            programChart.data = getChartData(program.getProgram())
            programChart.invalidate()

            checkBox.isChecked = false
            checkBox.setOnClickListener {

            }
        }

        /**
         * (<time>*<power>|<time>*<power>|...)
         */
        private fun getAveragePower(program: String): CharSequence {
            var avgPower = 0L
                program.split("|").forEach {
                    val power = it.split("*").last()
                    avgPower += power.toLong()
            }
            return "Average power: $avgPower"
        }

        private fun getChartData(program: String): LineData {
            val entries: ArrayList<Entry> = arrayListOf()
            program.split("|").forEach {
                val time = it.split("*").first().toFloat()
                val power = it.split("*").last().toFloat()
                entries.add(Entry(time, power))
            }
            val chart = LineDataSet(entries, "")
            return LineData(chart)
        }
    }

}