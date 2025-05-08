package com.antsfamily.biketrainer.util

const val STRING_EMPTY = ""

fun String?.orEmpty() = this ?: STRING_EMPTY
