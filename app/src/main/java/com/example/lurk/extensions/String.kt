package com.example.lurk.extensions

fun String.toTitleCase(): String = this.replaceFirstChar { it.titlecase() }