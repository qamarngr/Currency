package com.andela.currency.util

fun String.tryGetDouble(default: Double): Double {
    return try {
        this.toDouble()
    } catch (numberFormatEx: NumberFormatException) {
        default
    }
}