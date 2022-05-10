package com.example.lurk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.lurk.api.RedditApiConstants
import com.example.lurk.screens.*
import com.example.lurk.screens.feed.FeedScreen
import com.example.lurk.screens.feed.Post
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.NavBarItem.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val viewModel: LurkViewModel by viewModels()
    private val authManager = LurkApplication.instance().authManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pick our starting screen
        setContent {
            val controller = rememberSystemUiController()
            LurkTheme {
                val navController = rememberAnimatedNavController()
                val scope = rememberCoroutineScope()
                // A surface container using the 'background' color from the theme
                controller.setStatusBarColor(MaterialTheme.colorScheme.surface)
                controller.setNavigationBarColor(MaterialTheme.colorScheme.surface)
                val scaffoldState = rememberScaffoldState()

                // access listener
                val userHasAccess by authManager.userHasAccess.collectAsState()
                var expandedMedia by remember { mutableStateOf<Post?>(null) }

                val mainStatusColor by animateColorAsState(targetValue = if (expandedMedia == null) MaterialTheme.colorScheme.surface else Color.Black)
                LaunchedEffect(mainStatusColor) {
                    controller.setStatusBarColor(mainStatusColor)
                    controller.setNavigationBarColor(mainStatusColor)
                }
                Box {
                    Scaffold(
                        scaffoldState = scaffoldState,
                        bottomBar = {
                            AnimatedVisibility(
                                visible = userHasAccess,
                                enter = slideInVertically(initialOffsetY = { it / 2 }),
                                exit = fadeOut()
                            ) {
                                LurkBottomBar(navController = navController)
                            }
                        },
                        drawerShape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 12.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 12.dp
                        ),
                        drawerContent = {
                            val subreddits by viewModel.oUserSubreddits.collectAsState()
                            SubredditSelectionScreen(
                                subreddits = subreddits,
                                subredditSearchText = viewModel.subredditSearchText,
                                subredditSearchTextChange = viewModel::subredditSearchTextChange,
                                subredditSearchResults = viewModel.subredditSearchResults,
                                subredditFavoriteToggle = viewModel::toggleFavoriteSubreddit,
                                subredditSelected = { subreddit ->
                                    scope.launch {
                                        scaffoldState.drawerState.close()
                                    }
                                    viewModel.subredditSelected(subreddit)
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
                                    enterTransition = pageEnterTransition(),
                                    exitTransition = pageExitTransition()
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
                                    val feed by viewModel.oFeed.collectAsState()
                                    FeedScreen(
                                        feed = feed,
                                        loadingState = viewModel.feedLoadingState,
                                        updateVoteStatus = viewModel::voteStatusUpdated,
                                        subredditSelected = viewModel::subredditSelected,
                                        expandMedia = { post ->
                                            expandedMedia = post
                                        }
                                    )
                                }
                                composable(
                                    route = Account.route,
                                    enterTransition = pageEnterTransition(),
                                    exitTransition = pageExitTransition()
                                ) {
                                    Text("TODO FIX ME FIX ME PLS")
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
                ExpandedMediaScreen(post = expandedMedia) {
                    expandedMedia = null
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
        scaleIn(initialScale = 0.75f) +
        slideInVertically(initialOffsetY = { (it * 0.9f).toInt() }) +
        fadeIn(animationSpec = tween(500))
    }

    private fun pageExitTransition(): (AnimatedContentScope<NavBackStackEntry>.() -> ExitTransition?) = {
        scaleOut(targetScale = 0.75f) +
        slideOutVertically(targetOffsetY = { it }) +
        fadeOut(animationSpec = tween(200))
    }
}