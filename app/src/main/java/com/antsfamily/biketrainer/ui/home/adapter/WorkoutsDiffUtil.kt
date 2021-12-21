package com.antsfamily.biketrainer.ui.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.antsfamily.data.model.program.Program

class WorkoutsDiffUtil: DiffUtil.ItemCallback<Program>() {
    override fun areItemsTheSame(oldItem: Program, newItem: Program): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Program, newItem: Program): Boolean {
        return oldItem == newItem
    }
}
