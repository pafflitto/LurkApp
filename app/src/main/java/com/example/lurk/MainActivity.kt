package com.example.lurk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lurk.api.RedditApiConstants
import com.example.lurk.screens.*
import com.example.lurk.screens.expanded_media_screen.ExpandedMedia
import com.example.lurk.screens.expanded_media_screen.ExpandedMediaScreen
import com.example.lurk.screens.feed.feedScreen
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.LurkBottomBar
import com.example.lurk.ui_components.NavBarItem.Home
import com.example.lurk.viewmodels.FeedViewModel
import com.example.lurk.viewmodels.LoginViewModel
import com.gfycat.core.GfyCoreInitializationBuilder
import com.gfycat.core.GfyCoreInitializer
import com.gfycat.core.GfycatApplicationInfo
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalAnimationApi::class)
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        GfyCoreInitializer.initialize(
            GfyCoreInitializationBuilder(this, GfycatApplicationInfo(BuildConfig.GFYCAT_CLIENT_ID, BuildConfig.GFYCAT_CLIENT_SECRET))
        )

        setContent {
            val controller = rememberSystemUiController()
            LurkTheme(false, null) {
                val navController = rememberAnimatedNavController()
                val scope = rememberCoroutineScope()

                // A surface container using the 'background' color from the theme
                controller.setStatusBarColor(MaterialTheme.colorScheme.surface)
                controller.setNavigationBarColor(MaterialTheme.colorScheme.surface)
                val scaffoldState = rememberScaffoldState()

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                var expandedMedia by remember { mutableStateOf<ExpandedMedia?>(null) }

                val feedListState = rememberLazyListState()

                val mainStatusColor by animateColorAsState(
                    targetValue = if (expandedMedia == null) MaterialTheme.colorScheme.surface else Color.Black
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
                            LurkBottomBar(
                                navBackStackEntry = navBackStackEntry,
                                navController = navController,
                                feedListState = feedListState,
                                openDrawer = {
                                    scope.launch {
                                        scaffoldState.drawerState.open()
                                    }
                                }
                            )
                        },
                        drawerShape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 12.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 12.dp
                        ),
                        drawerGesturesEnabled = navBackStackEntry?.destination?.route == Home.route,
                        drawerContent = {
                            SubredditSelectionScreen(feedViewModel) {
                                scope.launch { scaffoldState.drawerState.close() }
                            }
                        }
                    ) {
                        Surface(modifier = Modifier.padding(it)) {
                            AnimatedNavHost(navController, startDestination = Screen.Splash.name) {
                                splashScreen(navController)
                                loginScreen(navController)

                                feedScreen(
                                    expandedMedia = expandedMedia,
                                    listState = feedListState,
                                    expandedMediaChange = { newMedia ->
                                        expandedMedia = newMedia
                                    },
                                    viewModel = feedViewModel
                                )
                                accountScreen()
                                settingsScreen()
                            }
                        }
                    }
                }
                ExpandedMediaScreen(showExpanded = expandedMedia != null, expandedMedia = expandedMedia) {
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
                loginViewModel.handleUserLoginResponse(code = code, error = error)
            }
        }
    }
}