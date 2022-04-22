package com.example.lurk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.lurk.api.RedditApiConstants
import com.example.lurk.screens.LoginScreen
import com.example.lurk.screens.LurkBottomBar
import com.example.lurk.screens.Screen
import com.example.lurk.screens.feed.FeedScreen
import com.example.lurk.screens.feed.SettingsScreen
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.NavBarItem.*
import com.example.lurk.viewmodels.FeedViewModel
import com.example.lurk.viewmodels.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()
    private val authManager = LurkApplication.instance().authManager

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pick our starting screen
        setContent {
            val controller = rememberSystemUiController()
            LurkTheme {
                val navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                controller.setStatusBarColor(MaterialTheme.colorScheme.surface)
                controller.setNavigationBarColor(MaterialTheme.colorScheme.surface)
                Scaffold(
                    bottomBar = {
                        LurkBottomBar(navController = navController)
                    }
                ) {
                    Surface {
                        NavHost(navController, startDestination = Screen.Splash.name) {
                            composable(Screen.Splash.name) {
                                SplashScreen(navController = navController)
                            }
                            composable(Screen.Login.name) {
                                LoginScreen(
                                    navController = navController,
                                    userlessLogin = viewModel::userlessLogin,
                                    login = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(RedditApiConstants.uriString))
                                        startActivity(intent)
                                    },
                                )
                            }
                            composable(Home.route) {
                                val posts = feedViewModel.posts.collectAsLazyPagingItems()
                                FeedScreen(
                                    posts = posts,
                                    updateVoteStatus = feedViewModel::voteStatusUpdated,
                                )
                            }
                            composable(Search.route) {
                                Text("TODO HELLO THERE")
                            }
                            composable(Account.route) {
                                Text("TODO FIX ME FIX ME PLS")
                            }
                            composable(Settings.route) {
                                val currentTheme by userPrefDataStore.userThemeFlow.collectAsState()
                                SettingsScreen(
                                    currentTheme = currentTheme,
                                    themeSelected = {
                                        viewModel.setUserTheme(it)
                                    }
                                )
                            }

                        }
                    }
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
    fun SplashScreen(navController: NavController) {
        // Empty screen for now
        val userHasAccess by authManager.userHasAccess.collectAsState()
        LaunchedEffect(userHasAccess) {
            navController.navigate(if (userHasAccess) Settings.label else Screen.Login.name)
        }
    }
}