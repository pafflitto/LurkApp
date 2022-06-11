package com.example.lurk.viewmodels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lurk.LurkApplication
import com.example.lurk.userPrefDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LurkViewModel : ViewModel() {
    private val authManager = LurkApplication.instance().authManager

    //region Login
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

    //region User Settings
    fun setUserTheme(theme: UserTheme) = viewModelScope.launch(Dispatchers.IO) {
        userPrefDataStore.saveTheme(theme)
    }
    //endregion
}

enum class UserTheme(val displayText: String, val icon: ImageVector) {
    Auto("Auto", Icons.Rounded.Schedule),
    Light("Light", Icons.Rounded.LightMode),
    Dark("Dark", Icons.Rounded.DarkMode),
    MaterialYou("Material You", Icons.Rounded.AutoAwesome)
}

data class UserSubreddit(val name: String, val favorited: Boolean? = null)

enum class SortingType {
    BEST,
    POPULAR,
    NEW,
    CONTROVERSIAL
}

enum class LoadingState {
    LOADING,
    LOADED,
    ERROR
}