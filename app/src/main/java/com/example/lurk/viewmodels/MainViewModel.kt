package com.example.lurk.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lurk.LurkApplication
import com.example.lurk.ui_components.NavBarItem
import com.example.lurk.ui_components.NavBarItem.Home
import com.example.lurk.userPrefDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val authManager = LurkApplication.instance().authManager

    fun userlessLogin() = viewModelScope.launch(Dispatchers.IO) {
        authManager.getAccess()
    }

    fun handleUserLoginResponse(code: String?, error: String?) = viewModelScope.launch(Dispatchers.IO) {
        if (error != null) {
            // TODO Add login states and show error
        }
        else if (code != null) {
            authManager.getAccess(code)
        }
    }

    var currentContentPage by mutableStateOf(Home)
    fun updateCurrentContentPage(item: NavBarItem) {
        currentContentPage = item
    }

    fun setUserTheme(theme: UserTheme) = viewModelScope.launch(Dispatchers.IO) {
        userPrefDataStore.saveTheme(theme)
    }
}

enum class UserTheme(val displayText: String, val icon: ImageVector) {
    Auto("Auto", Icons.Rounded.Schedule),
    Light("Light", Icons.Rounded.LightMode),
    Dark("Dark", Icons.Rounded.DarkMode),
    MaterialYou("Material You", Icons.Rounded.AutoAwesome)
}