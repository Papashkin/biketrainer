package com.antsfamily.biketrainer.ui.programs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antsfamily.biketrainer.R
import com.antsfamily.biketrainer.data.models.Program
import com.antsfamily.biketrainer.util.fullTimeFormat
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import java.io.File
import java.util.*
import javax.inject.Inject

class ProgramsAdapter @Inject constructor() :
    RecyclerView.Adapter<ProgramsAdapter.ProgramViewHolder>() {

    private var programs: ArrayList<Program> = arrayListOf()
    private var deletedPosition: Int = -1

    private var onEditClick: ((id: Int) -> Unit)? = null
    private var onDeleteClick: ((position: Int) -> Unit)? = null
    private var onItemClick: ((id: Int) -> Unit)? = null

    private lateinit var deletedItem: Program

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgramViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_program_info, parent, false)
        return ProgramViewHolder(view)
    }

    fun removeItem(position: Int) {
        deletedItem = programs[position]
        deletedPosition = position
        programs.removeAt(position)
        notifyItemRemoved(position)
        onDeleteClick?.invoke(position)
    }

    fun editItem(position: Int) {
        notifyDataSetChanged()
        onEditClick?.invoke(programs[position].getId())
    }

    fun setOnEditClickListener(listener: (id: Int) -> Unit) {
        onEditClick = listener
    }

    fun setOnDeleteClickListener(listener: (id: Int) -> Unit) {
        onDeleteClick = listener
    }

    fun setOnItemClickListener(listener: (id: Int) -> Unit) {
        onItemClick = listener
    }

//    fun undoDelete() {
//        programs.add(deletedPosition, deletedItem)
//        notifyDataSetChanged()
//    }

    override fun getItemCount(): Int = programs.size

    override fun onBindViewHolder(holder: ProgramViewHolder, position: Int) {
        holder.bind(this.programs[position])
    }

    fun setProgramList(newPrograms: List<Program>) {
        programs.clear()
        programs.addAll(newPrograms)
        notifyDataSetChanged()
    }

    inner class ProgramViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val programName = view.findViewById<TextView>(R.id.textProgramName)
        private val avgPower = view.findViewById<TextView>(R.id.textAveragePower)
        private val duration = view.findViewById<TextView>(R.id.textDuration)
        private val programImage = view.findViewById<ImageView>(R.id.imageProgram)
        private val maxPower = view.findViewById<TextView>(R.id.textMaxPower)
        private val programLayout = view.findViewById<View>(R.id.clProgram)

        fun bind(program: Program) {

            val programSource = program.getProgram()
            programName.text = program.getName()
            avgPower.text = getAveragePower(programSource)
            maxPower.text = getMaxPower(programSource)
            duration.text = getTotalTime(programSource)

            Picasso.get()
                .load(File(program.getImagePath()))
                .fit()
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(programImage)

            programLayout.setOnClickListener {
                onItemClick?.invoke(program.getId())
            }
        }

        /**
         * (<time>*<power>|<time>*<power>|...)
         */
        private fun getAveragePower(program: String): CharSequence {
            var avgPower = 0L
            var count = 0
            program.split("|").forEach {
                val power = it.split("*").last()
                if (power.isNotEmpty()) {
                    avgPower += power.toBigDecimal().toLong()
                    count += 1
                }
            }

            return "Average power: ${avgPower / count} W"
        }

        private fun getMaxPower(program: String): CharSequence {
            var maxPower = 0L
            program.split("|").forEach {
                val power = it.split("*").last()
                if (power.isNotEmpty()) {
                    if (power.toBigDecimal().toLong() > maxPower) {
                        maxPower = power.toBigDecimal().toLong()
                    }
                }
            }
            return "Max power: $maxPower W"
        }


        private fun getTotalTime(program: String): CharSequence {
            var count = 0.0f
            program.split("|").forEach {
                if (it.isNotEmpty()) {
                    count += it.split("*").first().toFloat()
                }
            }
            return "Total time: ${count.toLong().fullTimeFormat()}"
        }
    }

}