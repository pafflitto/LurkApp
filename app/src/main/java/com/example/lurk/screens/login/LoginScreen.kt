package com.example.lurk.screens

import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.example.lurk.data.api.RedditApiConstants
import com.example.lurk.extensions.bounceClick
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.viewmodels.LoginViewModel
import com.google.accompanist.navigation.animation.composable

// Navigation
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginScreen(
    navController: NavController,
) = composable(
    Screen.Login.name,
    enterTransition = { fadeIn() },
    exitTransition = { fadeOut() }
) {
    LoginScreen(navController)
}

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val hasAccess by viewModel.hasAccess.collectAsState()
    if (hasAccess) {
        navController.navigate("home")
    }
    LoginScreenContent(
        login = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(RedditApiConstants.uriString)
            )
            context.startActivity(intent)
        },
        userlessLogin = viewModel::userlessLogin
    )
}

@Composable
fun LoginScreenContent(
    login: () -> Unit,
    userlessLogin: () -> Unit
)
{
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Lurk for Reddit",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(
            onClick = login,
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = "Continue without signing in",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 128.dp)
                .bounceClick {
                    userlessLogin()
                }
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreviewDark()
{
    LurkTheme(useDarkPreviewTheme = true) {
        Surface {
            LoginScreen(NavController(LocalContext.current))
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun LoginScreenPreviewLight()
{
    LurkTheme(useDarkPreviewTheme = false) {
        Surface {
            LoginScreen(NavController(LocalContext.current))
        }
    }
}