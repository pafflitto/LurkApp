package com.example.lurk.screens

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lurk.ui.theme.LurkTheme
import com.example.lurk.ui_components.BouncingText

@Composable
fun LoginScreen(
    login: () -> Unit,
    userlessLogin:  () -> Unit
)
{
    LoginScreenContent(
        login,
        userlessLogin
    )
}

@Composable
fun LoginScreenContent(
    onLogin: () -> Unit,
    onUserlessLogin: () -> Unit
)
{
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Lurk for Reddit",
            style = MaterialTheme.typography.h1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Button(
            onClick = { onLogin() },
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.body1
            )
        }
        BouncingText(
            text = "Continue without signing in",
            modifier = Modifier.padding(top = 128.dp),
            clickEvent = {
                onUserlessLogin()
            }
        )
    }
}

@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreviewDark()
{
    LurkTheme(darkTheme = true) {
        Surface {
            LoginScreenContent({}, {})
        }
    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Composable
fun LoginScreenPreviewLight()
{
    LurkTheme(darkTheme = false) {
        Surface {
            LoginScreenContent({}, {})
        }
    }
}