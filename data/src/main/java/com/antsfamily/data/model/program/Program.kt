package com.antsfamily.data.model.program

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.concurrent.TimeUnit

/**
 * [Program] - data set class for trainings program
 * @param title - name of program (primary key)
 * @param data - List of [ProgramData], each of them contains power and duration
// * @param username - name of creator/main profile
 */
@Entity
data class Program(
    @PrimaryKey val title: String,
    val data: List<ProgramData>,
) {
    fun getAveragePower(): Int = data.sumOf { it.power }.div(data.size)

    fun getDuration(): String = data.sumOf { it.duration }.fullTimeFormat()


     /**
     * Time in format "0:00:00"
     */
    private fun Long.fullTimeFormat(): String {
        val hours = TimeUnit.SECONDS.toHours(this)
        val minutes = TimeUnit.SECONDS.toMinutes(this) - (TimeUnit.SECONDS.toHours(this) * 60)
        val seconds = TimeUnit.SECONDS.toSeconds(this) - (TimeUnit.SECONDS.toMinutes(this) * 60)

        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}
