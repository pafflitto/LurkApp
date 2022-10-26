package com.example.lurk.screens.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lurk.data.datastores.UserPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSettingsViewModel @Inject constructor(
    private val userPrefDataStore: UserPreferencesDataStore
) : ViewModel() {

    fun setUserTheme(theme: UserTheme) = viewModelScope.launch(Dispatchers.IO) {
        userPrefDataStore.saveTheme(theme)
    }

    val userTheme: StateFlow<UserTheme> = userPrefDataStore.userThemeFlow

}

enum class UserTheme(val displayText: String, val icon: ImageVector) {
    Auto("Auto", Icons.Rounded.Schedule),
    Light("Light", Icons.Rounded.LightMode),
    Dark("Dark", Icons.Rounded.DarkMode),
    MaterialYou("Material You", Icons.Rounded.AutoAwesome)
}
