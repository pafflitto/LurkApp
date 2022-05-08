package com.example.lurk


//region Double
fun Double.format(decimalPlaces: Int) = "%.${decimalPlaces}f".format(this)
//endregion

//region
fun String.toTitleCase(): String = this.replaceFirstChar { it.titlecase() }