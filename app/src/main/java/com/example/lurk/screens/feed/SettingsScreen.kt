package com.example.lurk.screens.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lurk.ui_components.ContentScreen
import com.example.lurk.viewmodels.UserTheme

@Composable
fun SettingsScreen(
    currentTheme: UserTheme,
    themeSelected: (UserTheme) -> Unit
) {
    // Theme Settings
    var openThemeDropDown by remember { mutableStateOf(false)}
    ContentScreen(title = "Settings") {
        SettingsRow(
            label = "Theme",
            onClick = { openThemeDropDown = true }
        ) {
            ThemeDropDown(
                openThemeDropDown = openThemeDropDown,
                closeDropdown = { openThemeDropDown = false},
                themeSelected = themeSelected
            )
        }
    }
}

@Composable
private fun SettingsRow(
    label: String,
    onClick: () -> Unit,
    Value: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(
                onClick = onClick
            )
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Value()
    }
}

@Composable
private fun ThemeDropDown(
    openThemeDropDown: Boolean,
    closeDropdown: () -> Unit,
    themeSelected: (UserTheme) -> Unit
) {
    DropdownMenu(
        expanded = openThemeDropDown,
        onDismissRequest = closeDropdown
    ) {
        Column {
            UserTheme.values().forEach {
                DropdownMenuItem(
                    text = {
                        Text(it.displayText)
                    },
                    leadingIcon = {
                        Icon(imageVector = it.icon, contentDescription = "${it.displayText} icon")
                    },
                    onClick = {
                        themeSelected(it)
                        closeDropdown()
                    }
                )
            }
        }
    }
}