package com.antsfamily.biketrainer.util

fun <T : Number> T?.toStringOrEmpty(pattern: String) = this?.toString() ?: pattern
