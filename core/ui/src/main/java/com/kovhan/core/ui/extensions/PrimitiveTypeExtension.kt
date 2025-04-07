package com.kovhan.core.ui.extensions

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

fun Any?.isNull(): Boolean {
    return this == null
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun BigInteger?.orEmpty(): BigInteger {
    return this ?: BigInteger.ZERO
}

fun Long?.orEmpty(): Long {
    return this ?: 0
}

fun Int?.orEmpty(): Int {
    return this ?: 0
}

fun BigDecimal?.orEmpty(): BigDecimal {
    return this ?: BigDecimal.ZERO
}

fun Double?.orEmpty(): Double {
    return this ?: 0.0
}

fun Float?.orEmpty(): Float {
    return this ?: 0F
}

fun Boolean?.orEmpty(): Boolean {
    return this ?: false
}

fun Char?.orEmpty(): Char {
    return this ?: ' '
}

fun Double.roundTo(scale: Int, roundingMode: RoundingMode = RoundingMode.UP): Double {
    return toBigDecimal().setScale(scale, roundingMode).toDouble()
}