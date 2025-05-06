package com.antsfamily.domain.antservice

import java.math.BigDecimal

fun BigDecimal?.orZero(): BigDecimal = this ?: BigDecimal.ZERO

@Suppress("UNCHECKED_CAST")
fun <T : Number> T?.orZero() = this ?: ZERO as T

fun Boolean?.orFalse() = this ?: false

private val ZERO: Number = 0
