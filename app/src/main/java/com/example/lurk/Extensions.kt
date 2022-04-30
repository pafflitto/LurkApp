package com.example.lurk

fun Double.format(decimalPlaces: Int) = "%.${decimalPlaces}f".format(this)