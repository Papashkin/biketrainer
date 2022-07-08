package com.antsfamily.biketrainer.util

fun <T : Number> T?.toStringOrEmpty(pattern: String) = this?.toString() ?: pattern

fun String?.orEmpty() = this ?: STRING_EMPTY

const val STRING_EMPTY = ""
