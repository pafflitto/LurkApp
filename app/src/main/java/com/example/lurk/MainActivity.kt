package com.example.lurk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.lurk.screens.LoginScreen
import com.example.lurk.screens.feed.FeedScreen
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.viewmodels.FeedViewModel
import com.example.lurk.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val feedViewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Pick our starting screen
        setContent {
            LurkTheme {
                // A surface container using the 'background' color from the theme
                Surface {
                    MainScreen(viewModel.accessGranted)
                }
            }
        }
    }

    @Composable
    fun MainScreen(accessGranted: Boolean)
    {
        if (accessGranted)
        {
            FeedScreen(feedViewModel.posts)
        }
        else
        {
            LoginScreen(viewModel::login, viewModel::userlessLogin)
        }
    }
}