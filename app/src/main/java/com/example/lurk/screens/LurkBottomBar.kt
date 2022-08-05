package com.example.lurk.screens

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.example.lurk.ui_components.NavBarItem
import kotlinx.coroutines.delay

@Composable
fun LurkBottomBar(
    navBackStackEntry: NavBackStackEntry?,
    navItemClick: (item: NavBarItem) -> Unit,
    longClick: (item: NavBarItem) -> Unit,
) {
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        NavBarItem.values().forEachIndexed { index, item ->

            val selected = currentRoute == item.route
            val interactionSource = remember { MutableInteractionSource() }
            val pressed by interactionSource.collectIsPressedAsState()
            var longPress by remember { mutableStateOf(false) }

            LaunchedEffect(pressed) {
                delay(400)
                if (pressed) {
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