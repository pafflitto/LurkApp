package com.example.lurk.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.lurk.ui.components.NavBarItem
import com.example.lurk.viewmodels.LoginViewModel
import com.google.accompanist.navigation.animation.composable

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

    Text(text = "Splash Screen") // TODO DO MORE HERE

    // Empty screen for now
    LaunchedEffect(Unit) {
        navController.navigate(if (hasAccess) NavBarItem.Home.route else Screen.Login.name)
    }
}