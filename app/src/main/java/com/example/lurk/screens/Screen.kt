package com.example.lurk.screens

import androidx.navigation.NavBackStackEntry

enum class Screen {
    Splash,
    Login,
    Home,
    Settings
}

fun NavBackStackEntry?.isPage(page: Screen): Boolean = this?.destination?.route == page.name