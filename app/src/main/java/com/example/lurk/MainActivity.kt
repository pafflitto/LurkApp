package com.example.lurk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lurk.api.RedditApiConstants
import com.example.lurk.screens.*
import com.example.lurk.screens.expanded_media_screen.ExpandedMedia
import com.example.lurk.screens.expanded_media_screen.ExpandedMediaScreen
import com.example.lurk.screens.feed.FeedScreen
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.NavBarItem.*
import com.example.lurk.viewmodels.FeedViewModel
import com.example.lurk.viewmodels.LurkViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    private val viewModel: LurkViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()
    private val authManager = LurkApplication.instance().authManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pick our starting screen
        setContent {
            val controller = rememberSystemUiController()
            LurkTheme(false, null) {
                val navController = rememberAnimatedNavController()
                val scope = rememberCoroutineScope()
                val hapticFeedback = LocalHapticFeedback.current

                // A surface container using the 'background' color from the theme
                controller.setStatusBarColor(MaterialTheme.colorScheme.surface)
                controller.setNavigationBarColor(MaterialTheme.colorScheme.surface)
                val scaffoldState = rememberScaffoldState()

                LaunchedEffect(scaffoldState.drawerState.currentValue) {
                    feedViewModel.updateDrawerState(scaffoldState.drawerState.currentValue)
                }

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                // access listener
                val userHasAccess by authManager.userHasAccess.collectAsState()
                var expandedMedia by remember { mutableStateOf<ExpandedMedia?>(null) }
                var showExpanded by remember { mutableStateOf(false) }

                val mainStatusColor by animateColorAsState(
                    targetValue = if (!showExpanded) MaterialTheme.colorScheme.surface else Color.Black
                )
                LaunchedEffect(mainStatusColor) {
                    controller.setStatusBarColor(mainStatusColor)
                    controller.setNavigationBarColor(mainStatusColor)
                }
                Box(
                    Modifier.background(MaterialTheme.colorScheme.surface)
                ) {
                    Scaffold(
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        scaffoldState = scaffoldState,
                        bottomBar = {
                            AnimatedVisibility(
                                visible = userHasAccess,
                                enter = slideInVertically(initialOffsetY = { it / 2 }),
                                exit = fadeOut()
                            ) {
                                LurkBottomBar(
                                    navBackStackEntry = navBackStackEntry,
                                    navItemClick = { item ->
                                        navController.backQueue.clear()

                                        when {
                                            item == Home && item.route == navBackStackEntry?.destination?.route -> {
                                                scope.launch {
                                                    feedViewModel.feedListState.animateScrollToItem(0)
                                                }
                                            }
                                            else -> {
                                                navController.navigate(item.route)
                                            }
                                        }
                                    },
                                    longClick = { item ->
                                        if (item == Home) {
                                            hapticFeedback.performHapticFeedback(
                                                HapticFeedbackType.LongPress
                                            )
                                            scope.launch {
                                                scaffoldState.drawerState.open()
                                            }
                                        }
                                    }
                                )
                            }
                        },
                        drawerShape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 12.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 12.dp
                        ),
                        drawerGesturesEnabled = navBackStackEntry?.destination?.route == Home.route,
                        drawerContent = {
                            val subreddits by feedViewModel.oUserSubreddits.collectAsState()
                            val currentSubreddit by feedViewModel.subredditFlow.collectAsState()
                            SubredditSelectionScreen(
                                subreddits = subreddits,
                                currentSubreddit = currentSubreddit,
                                subredditSearchText = feedViewModel.subredditSearchText,
                                subredditSearchTextChange = feedViewModel::subredditSearchTextChange,
                                subredditSearchResults = feedViewModel.subredditSearchResults,
                                subredditFavoriteToggle = feedViewModel::toggleFavoriteSubreddit,
                                subredditSelected = { subreddit ->
                                    feedViewModel.clearSubredditSearchText()
                                    scope.launch {
                                        scaffoldState.drawerState.close()
                                    }
                                    feedViewModel.subredditSelected(subreddit)
                                }
                            )
                        }
                    ) {
                        Surface(modifier = Modifier.padding(it)) {
                            AnimatedNavHost(navController, startDestination = Screen.Splash.name) {

                                composable(
                                    route = Screen.Splash.name,
                                    enterTransition = { fadeIn() },
                                    exitTransition = { fadeOut() }
                                ) {
                                    SplashScreen(
                                        navController = navController,
                                        userHasAccess = userHasAccess
                                    )
                                }

                                composable(
                                    Screen.Login.name,
                                    enterTransition = { fadeIn() },
                                    exitTransition = { fadeOut() }
                                ) {
                                    LoginScreen(
                                        navController = navController,
                                        userlessLogin = viewModel::userlessLogin,
                                        login = {
                                            val intent = Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse(RedditApiConstants.uriString)
                                            )
                                            startActivity(intent)
                                        },
                                    )
                                }

                                composable(
                                    route = Home.route,
                                    enterTransition = pageEnterTransition(),
                                    exitTransition = pageExitTransition()
                                ) {
                                    val feed by feedViewModel.oFeed.collectAsState()
                                    FeedScreen(
                                        feed = feed,
                                        listState = feedViewModel.feedListState,
                                        loadingState = feedViewModel.feedLoadingState,
                                        updateVoteStatus = feedViewModel::voteStatusUpdated,
                                        subredditSelected = feedViewModel::subredditSelected,
                                        expandMedia = { clickedMedia ->
                                            showExpanded = true
                                            expandedMedia = clickedMedia
                                        },
                                        expandedMedia = expandedMedia,
                                        updateTopVisibleItems = feedViewModel::updateVisibleItems,
                                        gifExoPlayers = feedViewModel.gifExoPlayers
                                    )
                                }

                                composable(
                                    route = Account.route,
                                    enterTransition = pageEnterTransition(),
                                    exitTransition = pageExitTransition()
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxSize(),
                                        text = "TODO FIX ME FIX ME PLS")
                                }

                                composable(
                                    Settings.route,
                                    enterTransition = pageEnterTransition(),
                                    exitTransition = pageExitTransition()
                                ) {
                                    val currentTheme by userPrefDataStore.userThemeFlow.collectAsState()
                                    SettingsScreen(
                                        currentTheme = currentTheme,
                                        themeSelected = { selectedTheme ->
                                            viewModel.setUserTheme(selectedTheme)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                ExpandedMediaScreen(showExpanded = showExpanded, expandedMedia = expandedMedia) {
                    expandedMedia = null
                    showExpanded = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        intent.data?.let { uri ->
            val state = uri.getQueryParameter("state")
            val code = uri.getQueryParameter("code")
            val error = uri.getQueryParameter("error")
            if (state == RedditApiConstants.STATE) {
                viewModel.handleUserLoginResponse(code = code, error = error)
            }
        }
    }

    @Composable
    fun SplashScreen(
        navController: NavController,
        userHasAccess: Boolean,
    ) {
        // Empty screen for now
        LaunchedEffect(userHasAccess) {
            navController.navigate(if (userHasAccess) Home.route else Screen.Login.name)
        }
    }

    private fun pageEnterTransition(): (AnimatedContentScope<NavBackStackEntry>.() -> EnterTransition?) = {
        slideInVertically(initialOffsetY = { (it * 0.1f).toInt() }) +
        fadeIn(animationSpec = tween(200))
    }

    private fun pageExitTransition(): (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?) = {
        scaleOut(
            animationSpec = tween(200),
            targetScale = 0.6f
        )
        fadeOut(animationSpec = tween(200))
    }
}