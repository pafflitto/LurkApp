package com.example.lurk.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lurk.ui_components.NavBarItem

@Composable
fun ContentScreen(
    navController: NavController,
    selectedItem: Int,
    Content: @Composable (Modifier) -> Unit
) {
    var selected by remember { mutableStateOf(0) } // TODO move this to vm
    Surface(
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column {
            Content(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                NavBarItem.values().forEachIndexed { index, item ->
                    NavigationBarItem(
                        alwaysShowLabel = false,
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label + "icon"
                            )
                        },
                        label = { Text(item.label) },
                        selected = index == selected,
                        onClick = { selected = index },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }
    }
}