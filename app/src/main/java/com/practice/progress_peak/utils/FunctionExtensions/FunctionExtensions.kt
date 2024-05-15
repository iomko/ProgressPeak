package com.practice.progress_peak.utils.FunctionExtensions

fun Number.formatCount(): String {
    return when (this) {
        is Int -> toString()
        is Float, is Double -> "%.2f".format(this)
        else -> throw IllegalArgumentException("Unsupported number type")
    }
}