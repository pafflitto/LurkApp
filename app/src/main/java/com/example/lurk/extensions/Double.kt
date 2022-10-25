package com.example.lurk.extensions

fun Double.format(decimalPlaces: Int) = "%.${decimalPlaces}f".format(this)