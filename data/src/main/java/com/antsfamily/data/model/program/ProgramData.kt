package com.antsfamily.data.model.program

data class ProgramData(
    val power: Int,
    val duration: Long
)

fun List<ProgramData>.getTotalDuration() = sumOf { it.duration }
fun List<ProgramData>.getPowerMax() = maxOf { it.power }
