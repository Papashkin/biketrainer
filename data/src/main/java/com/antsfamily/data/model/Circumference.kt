package com.antsfamily.data.model

import java.math.BigDecimal

enum class Circumference(val title: String?, val value: BigDecimal) {
    WHEEL_700x23("700c x 23mm", BigDecimal(2.097)),
    WHEEL_700x25("700c x 25mm", BigDecimal(2.105)),
    WHEEL_700x28("700c x 28mm", BigDecimal(2.136)),
    WHEEL_700x32("700c x 32mm", BigDecimal(2.155)),
    WHEEL_700x35("700c x 35mm", BigDecimal(2.168)),
    WHEEL_700x38("700c x 38mm", BigDecimal(2.180)),
    WHEEL_700x44("700c x 44mm", BigDecimal(2.224)),
    WHEEL_700x50("700c x 50mm", BigDecimal(2.293)),
    UNKNOWN(null, BigDecimal.ZERO),
    ;

    override fun toString(): String = this.title.orEmpty()
}
