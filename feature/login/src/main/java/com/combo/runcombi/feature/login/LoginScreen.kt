package com.combo.runcombi.feature.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.ext.screenDefaultPadding
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun LoginRoute(
    onLoginSuccess: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    loginManager: LoginManager = rememberLoginManager(),
    viewModel: LoginViewModel = hiltViewModel(),
) {
    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest {
            when (it) {
                LoginEvent.Error -> Unit
                is LoginEvent.Success -> onLoginSuccess(it.isFinishedRegister)
            }
        }
    }

    LoginScreen(
        modifier = modifier,
        onKakaoLoginClick = {
            coroutineScope.launch {
                viewModel.login(loginManager.request())
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
            .fillMaxSize()
    ) {
        StableImage(
            drawableResId = R.drawable.login_app_logo,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .align(Alignment.Center)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .screenDefaultPadding()
                .align(Alignment.BottomCenter)
        ) {
            Button(
                onClick = onKakaoLoginClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFEE102),
                    contentColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(6.dp)
            ) {
                StableImage(
                    drawableResId = R.drawable.ic_kakao,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = stringResource(id = R.string.kakao_login),
                    style = RunCombiTypography.body1,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        onKakaoLoginClick = {

        }
    )
}
