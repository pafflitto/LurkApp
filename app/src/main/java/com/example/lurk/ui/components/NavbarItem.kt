package com.example.lurk.ui.components

import com.example.lurk.R

enum class NavBarItem(val label: String, val selectedIcon: Int, val defaultIcon: Int, val route: String) {
    Home(label = "Home", selectedIcon = R.drawable.ic_home_filled ,defaultIcon = R.drawable.ic_home_outline, route = "home"),
    Account(label = "Account", selectedIcon = R.drawable.ic_person_filled, defaultIcon = R.drawable.ic_person_outline, route = "account"),
    Settings(label = "Settings", selectedIcon = R.drawable.ic_settings_filled, defaultIcon = R.drawable.ic_settings_outline, route = "settings")
}