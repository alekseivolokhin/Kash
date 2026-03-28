package com.volokhinaleksey.kash.presentation.util

import kotlin.math.abs
import kotlin.math.roundToLong

fun formatTenge(amount: Double): String {
    val rounded = abs(amount).roundToLong()
    val formatted = rounded.toString().reversed().chunked(3).joinToString(" ").reversed()
    return "$formatted \u20B8"
}

fun formatTengeWithSign(amount: Double, isIncome: Boolean): String {
    val sign = if (isIncome) "+" else "-"
    return "$sign${formatTenge(amount)}"
}
