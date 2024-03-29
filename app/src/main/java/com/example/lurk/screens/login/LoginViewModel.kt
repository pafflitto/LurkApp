package com.example.lurk.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lurk.data.repositories.RedditAuthRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepo: RedditAuthRepo,
) : ViewModel() {

    fun userlessLogin() = viewModelScope.launch(Dispatchers.IO) {
        authRepo.requestUserlessToken()
    }

    fun handleUserLoginResponse(code: String?, error: String?) = viewModelScope.launch(Dispatchers.IO) {
        code?.let {
            authRepo.requestUserToken(it)
        } ?: run {
            // TODO Add login states and show error
        }
    }

    val hasAccess: StateFlow<Boolean> = authRepo.userHasAccess(viewModelScope)
}

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