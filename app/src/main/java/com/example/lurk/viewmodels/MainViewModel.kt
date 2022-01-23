package com.example.lurk.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lurk.LurkApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val authManager = LurkApplication.instance().authManager

    // Value that is listened to by the MainScreen composable in MainActivity to tell if we should show feeds
    var accessGranted by mutableStateOf(false)

    init {
        viewModelScope.launch {
            authManager.accessTokenValid.collect {
                accessGranted = it
            }
        }
    }

    fun userlessLogin() = viewModelScope.launch(Dispatchers.IO) {
        accessGranted = authManager.getAccess()
    }

    fun login() = viewModelScope.launch(Dispatchers.IO) {

    }
}