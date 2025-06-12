package com.antsfamily.biketrainer.ui.createworkout.model

data class Duration(
    val minutes: Int,
    val seconds: Int
) {
    companion object {
        val Empty = Duration(0,0)
    }

    val total: Int
        get() = minutes.times(60).plus(seconds)

    val isZero: Boolean
        get() = minutes == 0 && seconds == 0

    val isEmpty: Boolean = this == Empty
}

fun Duration?.orEmpty(): Duration = this ?: Duration.Empty

fun Long.toDuration(): Duration {
    val minutes = this / 60
    val seconds = this % 60

    return Duration(minutes.toInt(), seconds.toInt())
}

fun Int.toDuration(): Duration {
    val minutes = this / 60
    val seconds = this % 60

    return Duration(minutes, seconds)
}