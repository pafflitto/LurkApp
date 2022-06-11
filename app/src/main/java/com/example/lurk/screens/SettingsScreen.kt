package com.example.lurk.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.rounded.FormatPaint
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.lurk.viewmodels.UserTheme
import com.example.lurk.ui_components.MainPageScreen

@Composable
fun SettingsScreen(
    currentTheme: UserTheme,
    themeSelected: (UserTheme) -> Unit
) {
    // Theme Settings
    MainPageScreen(title = "Settings") {
        var showingOptions by remember { mutableStateOf(false) }
        SettingsRow(
            label = "Theme",
            value = currentTheme.displayText,
            icon = Icons.Rounded.FormatPaint,
            showingOptions = showingOptions,
            showOptions = { showOptions ->
                showingOptions = showOptions
            }
            ) { lazyListScope ->
            lazyListScope.itemsIndexed(UserTheme.values()) { index, item ->
                SettingsOptionItem(
                    icon = item.icon,
                    option = item.displayText,
                    optionSelected = {
                        themeSelected(item)
                        showingOptions = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(
    label: String,
    value: String,
    icon: ImageVector,
    showingOptions: Boolean,
    showOptions: (showOptions: Boolean) -> Unit,
    optionView: (LazyListScope) -> Unit
) {
    Row(
        modifier = Modifier
            .clickable(
                onClick = {
                    showOptions(true)
                },
                enabled = !showingOptions
            )
            .height(SettingsScreenConstants.rowHeight)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = icon , contentDescription = "Settings Icon", modifier = Modifier.padding(start = 16.dp))

        Box {
            val alpha by animateFloatAsState(if (showingOptions) 0f else 1f)
            Row(
                Modifier.alpha(alpha).padding(end = 16.dp).align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = value,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }
            androidx.compose.animation.AnimatedVisibility(
                visible = showingOptions,
                enter = slideInHorizontally(
                    initialOffsetX = {
                        it
                    },
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { it }
                )
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box {
                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(end = 32.dp)
                        ) {
                            optionView(this)
                        }
                        Box(
                            Modifier
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                .align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Cancel,
                                contentDescription = "Close options",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .clickable(
                                        onClick = { showOptions(false) },
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    )
                                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                                    .fillMaxHeight()
                                    .padding(end = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsOptionItem(
    icon: ImageVector,
    option: String,
    optionSelected: () -> Unit
) {
    Row(
        modifier = Modifier
            .clickable {
                optionSelected()
            }
            .height(SettingsScreenConstants.rowHeight)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Settings Option Icon"
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(option)
    }
}

object SettingsScreenConstants {
    val rowHeight = 72.dp
}