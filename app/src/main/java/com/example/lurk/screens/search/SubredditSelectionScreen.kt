@file:OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class
)

package com.example.lurk.screens.search

import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.lurk.R
import com.example.lurk.screens.feed.FeedViewModel
import com.example.lurk.screens.feed.UserSubreddit

@Composable
fun SubredditSelectionScreen(
    viewModel: FeedViewModel,
    closeDrawer: () -> Unit
) {
    val subreddits by viewModel.userSubreddits.collectAsState()
    SubredditSelectionScreenContent(
        currentSubreddit = viewModel.currentSubreddit,
        subreddits = subreddits,
        subredditSearchText = viewModel.subredditSearchText,
        subredditSearchTextChange = viewModel::searchForSubreddit,
        subredditSearchResults = viewModel.subredditSearchResults,
        subredditFavoriteToggle = viewModel::toggleFavoriteSubreddit,
        subredditSelected = {
            closeDrawer()
            viewModel.clearSubredditSearchText()
            viewModel.updateSubreddit(it)
        }
    )
}

@Composable
fun SubredditSelectionScreenContent(
    currentSubreddit: String,
    subreddits: Map<String, List<UserSubreddit>>,
    subredditSearchText: String,
    subredditSearchTextChange: (String) -> Unit,
    subredditSearchResults: List<String>,
    subredditFavoriteToggle: (subreddit: String, currentlyFavorited: Boolean) -> Unit,
    subredditSelected: (subreddit: String) -> Unit
) {
    var searchOpen by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current // Focus manager to use with search text field
    val focusRequester = remember { FocusRequester() } // focus requester for textfield

    Surface(modifier = Modifier.fillMaxSize()) {

        AnimatedVisibility(visible = subreddits.isEmpty(), exit = fadeOut()) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Center))
            }
        }

        AnimatedVisibility(
            visible = subreddits.isNotEmpty(),
            enter = slideInVertically(initialOffsetY = { it / 2 })
        ) {
            Box {
                AnimatedVisibility(
                    visible = !searchOpen,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                    ) {
                        item {
                            Text(
                                text = "Subreddits",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        item {
                            SubredditCard(
                                userSubreddit = UserSubreddit(currentSubreddit, null),
                                subredditSelected = {},
                                subredditFavoriteToggle = {_,_ ->},
                                currentSubreddit = true,
                                modifier = Modifier.animateItemPlacement().padding(top = 8.dp)
                            )
                        }
                        subreddits.forEach { entry ->
                            item {
                                Text(
                                    text = entry.key,
                                    modifier = Modifier.padding(top = 16.dp)
                                )
                            }

                            items(
                                items = entry.value,
                            ) { item ->
                                SubredditCard(
                                    modifier = Modifier.animateItemPlacement(),
                                    userSubreddit = item,
                                    subredditSelected = {
                                        focusManager.clearFocus(true)
                                        subredditSelected(it)
                                    },
                                    subredditFavoriteToggle = subredditFavoriteToggle,
                                )
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }

                Box(Modifier.fillMaxSize()) {
                    AnimatedVisibility(
                        visible = searchOpen,
                        enter = slideInHorizontally(
                            animationSpec = tween(durationMillis = 500),
                            initialOffsetX = { -it }),
                        exit = slideOutHorizontally(targetOffsetX = { -it })
                    )
                    {
                        Box(
                            Modifier
                                .clickable {
                                    searchOpen = false
                                }
                                .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                .background(color = MaterialTheme.colorScheme.primary)
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "Back",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(16.dp)
                            )
                        }
                    }

                    LaunchedEffect(searchOpen) {
                        focusManager.clearFocus(true)
                    }

                    AnimatedVisibility(
                        visible = searchOpen,
                        enter = slideInHorizontally(initialOffsetX = { -it }),
                        exit = slideOutHorizontally(targetOffsetX = { -it })
                    ) {
                        val keyboardController = LocalSoftwareKeyboardController.current
                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight()
                        ) {
                            TextField(
                                value = subredditSearchText,
                                onValueChange = subredditSearchTextChange,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Rounded.Search,
                                        contentDescription = "Search Icon"
                                    )
                                },
                                modifier = Modifier
                                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp)
                                    .focusRequester(focusRequester)
                                    .onFocusChanged {
                                        if (it.hasFocus) {
                                            keyboardController?.show()
                                        }
                                    },
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    containerColor = Color.Transparent,
                                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                    unfocusedIndicatorColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Search,
                                ),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        focusManager.clearFocus()
                                    }
                                )
                            )
                            LazyColumn(
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                            ) {
                                if (subredditSearchResults.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "Results",
                                            Modifier.padding(top = 16.dp)
                                        )
                                    }
                                }

                                items(
                                    items = subredditSearchResults,
                                    key = {
                                        it
                                    }
                                ) { name ->
                                    SubredditCard(
                                        modifier = Modifier.animateItemPlacement(),
                                        userSubreddit = UserSubreddit(name),
                                        subredditSelected = {
                                            searchOpen = false
                                            subredditSelected(it)
                                            focusManager.clearFocus(true)
                                        },
                                        subredditFavoriteToggle = subredditFavoriteToggle,
                                        fromSearch = true
                                    )
                                }
                            }

                            LaunchedEffect(Unit) {
                                focusRequester.requestFocus()
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = !searchOpen,
                    enter = scaleIn(animationSpec = spring()),
                    exit = scaleOut(animationSpec = tween(200), 0f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 16.dp, bottom = 16.dp)
                ) {
                    LargeFloatingActionButton(
                        onClick = {
                            searchOpen = !searchOpen
                        },
                        containerColor = MaterialTheme.colorScheme.primary,

                        ) {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = "Search Icon")
                    }
                }
            }
        }
    }
}

@Composable
private fun SubredditCard(
    modifier: Modifier = Modifier,
    userSubreddit: UserSubreddit,
    subredditSelected: (subreddit: String) -> Unit,
    subredditFavoriteToggle: (subreddit: String, currentlyFavorited: Boolean) -> Unit,
    currentSubreddit: Boolean = false,
    fromSearch: Boolean = false,
) {
    Column(
        modifier = modifier
    ) {
        Card(
            onClick = {
                subredditSelected(userSubreddit.name)
            },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (currentSubreddit) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(verticalAlignment = CenterVertically, modifier = Modifier
                .height(48.dp)
                .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = userSubreddit.name,
                    modifier = Modifier
                        .weight(1f)
                )

                userSubreddit.favorited?.let { favorited ->
                    IconButton(
                        onClick = {
                            subredditFavoriteToggle(userSubreddit.name, favorited)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = if (favorited) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_outline),
                            contentDescription = "Favorite Subreddit Icon"
                        )
                    }
                }

                if (fromSearch){
                    Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = "Go To Subreddit Icon", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}