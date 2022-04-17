package com.example.lurk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.lurk.screens.ContentScreen
import com.example.lurk.screens.LoginScreen
import com.example.lurk.screens.feed.FeedScreen
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.viewmodels.FeedViewModel
import com.example.lurk.viewmodels.MainViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()
    private val authManager = LurkApplication.instance().authManager

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
                Surface{
                    NavHost(navController, startDestination = "splash_screen") {
                        composable(route = "splash_screen") {
                            SplashScreen(navController = navController)
                        }
                        composable(route = "login") {
                            LoginScreen(
                                navController = navController,
                                userlessLogin = viewModel::userlessLogin,
                                login = {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(RedditApiConstants.uriString))
                                    startActivity(intent)
                                },
                            )
                        }
                        composable("home") {
                            val posts = feedViewModel.posts.collectAsLazyPagingItems()
                            ContentScreen(navController = navController, selectedItem = 0) { modifier ->
                                FeedScreen(
                                    posts = posts,
                                    updateVoteStatus = feedViewModel::voteStatusUpdated,
                                    modifier = modifier
                                )
                            }
                        }
                    }
                }
                val userHasAccess by authManager.userHasAccess.collectAsState()
                LaunchedEffect(userHasAccess) {
                    navController.navigate(if (userHasAccess) "home" else "login")
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
    }
}