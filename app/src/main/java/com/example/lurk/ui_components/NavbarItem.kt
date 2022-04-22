package com.example.lurk.ui_components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavBarItem(val label: String, val icon: ImageVector, val route: String) {
    Home("Home", Icons.Rounded.Home, "home"),
    Search("Search", Icons.Rounded.Search, "search"),
    Account("Account", Icons.Rounded.AccountCircle, "account"),
    Settings("Settings", Icons.Rounded.Settings, "settings")
}