package com.example.lurk.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lurk.LurkApplication
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
}