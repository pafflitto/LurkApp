package com.example.lurk

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper


//region Double
fun Double.format(decimalPlaces: Int) = "%.${decimalPlaces}f".format(this)
//endregion

//region String
fun String.toTitleCase(): String = this.replaceFirstChar { it.titlecase() }
// endregion

//region Context
fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
// endregion