package com.example.lurk.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.lurk.screens.login.LoginViewModel
import com.example.lurk.ui.components.NavBarItem
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.delay

// Navigation
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.splashScreen(navController: NavController) = composable(
    route = Screen.Splash.name,
    enterTransition = { fadeIn() },
    exitTransition = { fadeOut() }
) {
    SplashScreen(
        navController = navController,
    )
}

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val hasAccess by viewModel.hasAccess.collectAsState()

    // Empty screen for now
    LaunchedEffect(Unit) {
        delay(1000)
        navController.navigate(if (hasAccess) NavBarItem.Home.route else Screen.Login.name)
    }

    Box(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Splash Screen"
        )
    }
}