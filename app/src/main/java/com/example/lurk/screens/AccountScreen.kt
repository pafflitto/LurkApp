package com.example.lurk.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import com.example.lurk.ui_components.NavBarItem
import com.example.lurk.ui_components.pageEnterTransition
import com.example.lurk.ui_components.pageExitTransition
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.accountScreen() = composable(
    route = NavBarItem.Account.route,
    enterTransition = pageEnterTransition(),
    exitTransition = pageExitTransition()
) {
    Text(
        modifier = Modifier.fillMaxSize(),
        text = "TODO FIX ME FIX ME PLS")
}