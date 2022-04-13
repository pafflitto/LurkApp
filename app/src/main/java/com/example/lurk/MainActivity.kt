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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.lurk.api.RedditApiConstants
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
                // A surface container using the 'background' color from the theme
                controller.setStatusBarColor(MaterialTheme.colorScheme.surface)
                Surface {
                    val userHasAccess by authManager.userHasAccess.collectAsState(initial = false)
                    MainScreen(userHasAccess)
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
    fun MainScreen(accessGranted: Boolean) {
        if (accessGranted) {
            FeedScreen(
                subreddit = feedViewModel.subreddit,
                posts = feedViewModel.posts,
                updateVoteStatus = feedViewModel::voteStatusUpdated
            )
        }
        else {
            LoginScreen(
                userlessLogin = viewModel::userlessLogin,
                login = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(RedditApiConstants.uriString))
                    startActivity(intent)
                }
            )
        }
    }
}