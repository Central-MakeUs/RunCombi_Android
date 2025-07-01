package com.combo.runcombi.feature.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    loginManager: LoginManager = rememberLoginManager(),
    viewModel: LoginViewModel = hiltViewModel(),
) {
    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                LoginEvent.Error -> Unit
                is LoginEvent.Success -> onLoginSuccess()
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        onKakaoLoginClick = {
            coroutineScope.launch {
                viewModel.login()
            }
        }
    )
}

@Composable
private fun rememberLoginManager(): LoginManager {
    val context = LocalContext.current
    return remember {
        LoginManager(context)
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onKakaoLoginClick: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onKakaoLoginClick = {}
    )
}
