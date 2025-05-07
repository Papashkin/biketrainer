package com.antsfamily.biketrainer.core.model

import androidx.annotation.StringRes
import com.antsfamily.biketrainer.R
import java.math.BigDecimal

enum class Circumference(val value: BigDecimal) {
    WHEEL_700x23(BigDecimal(2.097)),
    WHEEL_700x25(BigDecimal(2.105)),
    WHEEL_700x28(BigDecimal(2.136)),
    WHEEL_700x32(BigDecimal(2.155)),
    WHEEL_700x35(BigDecimal(2.168)),
    WHEEL_700x38(BigDecimal(2.180)),
    WHEEL_700x44(BigDecimal(2.224)),
    WHEEL_700x50(BigDecimal(2.293)),
    UNKNOWN(BigDecimal.ZERO),
    ;

}

@StringRes
fun Circumference.toStringId(): Int = when (this) {
        Circumference.WHEEL_700x23 -> R.string.circumference_700x23
        Circumference.WHEEL_700x25 -> R.string.circumference_700x25
        Circumference.WHEEL_700x28 -> R.string.circumference_700x28
        Circumference.WHEEL_700x32 -> R.string.circumference_700x32
        Circumference.WHEEL_700x35 -> R.string.circumference_700x35
        Circumference.WHEEL_700x38 -> R.string.circumference_700x38
        Circumference.WHEEL_700x44 -> R.string.circumference_700x44
        Circumference.WHEEL_700x50 -> R.string.circumference_700x50
        Circumference.UNKNOWN -> R.string.circumference_unknown
    }
