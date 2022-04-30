package com.example.lurk.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lurk.ui_components.NavBarItem

@Composable
fun LurkBottomBar(
    navController: NavController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp
    ) {
        NavBarItem.values().forEachIndexed { index, item ->
            val selected = currentRoute == item.route
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
                    navController.backQueue.clear()
                    navController.navigate(item.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}