package com.example.lurk.ui_components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

@OptIn(ExperimentalAnimationApi::class)
fun pageEnterTransition(): (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?) = {
    slideInVertically(initialOffsetY = { (it * 0.1f).toInt() }) +
            fadeIn(animationSpec = tween(200))
}

@OptIn(ExperimentalAnimationApi::class)
fun pageExitTransition(): (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?) = {
    scaleOut(
        animationSpec = tween(200),
        targetScale = 0.6f
    )
    fadeOut(animationSpec = tween(200))
}