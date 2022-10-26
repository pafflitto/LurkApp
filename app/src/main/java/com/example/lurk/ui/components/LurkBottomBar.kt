package com.example.lurk.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.lurk.viewmodels.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LurkBottomBar(
    navBackStackEntry: NavBackStackEntry?,
    navController: NavController,
    feedListState: LazyListState,
    openDrawer: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val hasAccess by viewModel.hasAccess.collectAsState()
    val scope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current

    AnimatedVisibility(
        visible = hasAccess,
        enter = slideInVertically(initialOffsetY = { it / 2 }),
        exit = fadeOut()
    ) {
        LurkBottomBarContent(
            navBackStackEntry = navBackStackEntry,
            navItemClick = { item ->
                navController.backQueue.clear()

                when {
                    item == NavBarItem.Home && item.route == navBackStackEntry?.destination?.route -> {
                        scope.launch {
                            feedListState.animateScrollToItem(0)
                        }
                    }
                    else -> {
                        navController.navigate(item.route)
                    }
                }
            },
            longClick = { item ->
                if (item == NavBarItem.Home) {
                    hapticFeedback.performHapticFeedback(
                        HapticFeedbackType.LongPress
                    )
                    openDrawer()
                }
            }
        )
    }
}

@Composable
private fun LurkBottomBarContent(
    navBackStackEntry: NavBackStackEntry?,
    navItemClick: (item: NavBarItem) -> Unit,
    longClick: (item: NavBarItem) -> Unit,
) {
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.surface.copy(0.33f)
                    )
                )
            )
            .fillMaxWidth(),
        contentColor = Color.Transparent,
        containerColor = Color.Transparent,
        tonalElevation = 0.dp
    ) {
        NavBarItem.values().forEachIndexed { index, item ->

            val selected = currentRoute == item.route
            val interactionSource = remember { MutableInteractionSource() }
            val pressed by interactionSource.collectIsPressedAsState()
            var longPress by remember { mutableStateOf(false) }

            LaunchedEffect(pressed) {
                delay(200)
                if (pressed && currentRoute == NavBarItem.Home.route) {
                    longPress = true
                    longClick(item)
                }
            }
            NavigationBarItem(
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        painter = painterResource(id = if (selected) item.selectedIcon else item.defaultIcon),
                        contentDescription = item.label + "icon"
                    )
                },
                label = { Text(item.label) },
                selected = selected,
                onClick = {
                    if (!longPress) {
                        navItemClick(item)
                    } else {
                        longPress = false
                    }
                },
                interactionSource = interactionSource,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}