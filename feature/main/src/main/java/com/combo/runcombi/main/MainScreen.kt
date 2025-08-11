package com.combo.runcombi.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey07
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.main.navigation.MainNavHost
import com.combo.runcombi.main.navigation.MainNavigator
import com.combo.runcombi.main.navigation.rememberMainNavigator
import com.combo.runcombi.main.navigation.compareTo
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import androidx.core.net.toUri
import com.combo.runcombi.core.designsystem.theme.Grey01

@OptIn(FlowPreview::class)
@Composable
internal fun MainScreen(
    startDestination: RouteModel,
    navigator: MainNavigator = rememberMainNavigator(),
    viewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val showForceUpdateDialog by viewModel.showForceUpdateDialog.collectAsState()

    LaunchedEffect(Unit) {
        val versionName = try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        } catch (e: Exception) {
            "1.0.0"
        } ?: "1.0.0"

        viewModel.checkAppVersion(versionName)
        
        viewModel.navigationEvent.debounce(200).collectLatest { route ->
            if (navigator.navController.currentDestination?.compareTo(route) == false) {
                if (route == RouteModel.Login) {
                    navigator.navigateToLogin()
                } else {
                    navigator.navController.navigate(route)
                }
            }
        }
    }

    MainScreenContent(
        navigator = navigator,
        startDestination = startDestination
    )

    if (showForceUpdateDialog) {
        ForceUpdateDialog(
            onUpdate = {
                try {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                        data =
                            "https://play.google.com/store/apps/details?id=com.combo.runcombi".toUri()
                        addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        )
    }
}

@Composable
private fun MainScreenContent(
    navigator: MainNavigator,
    startDestination: RouteModel,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        containerColor = Grey01,
        modifier = modifier,
        content = { padding ->
            MainNavHost(
                navigator = navigator,
                startDestination = startDestination,
                padding = padding
            )
        }
    )
}

@Composable
private fun ForceUpdateDialog(
    onUpdate: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {
            android.os.Process.killProcess(android.os.Process.myPid())
        },
        containerColor = Grey02,
        title = {
            Text(
                text = "업데이트가 필요합니다",
                style = title2,
                color = Grey08,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "새로운 버전이 출시되었습니다.\n앱을 최신 버전으로 업데이트해주세요.",
                    style = body1,
                    color = Grey07,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "업데이트 후 앱을 다시 실행할 수 있습니다.",
                    style = body1,
                    color = Grey07.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            RunCombiButton(
                text = "스토어에서 업데이트",
                onClick = onUpdate,
                modifier = Modifier.fillMaxWidth(),
                enabledColor = Primary01,
                textColor = Grey02
            )
        },
        modifier = Modifier.padding(20.dp)
    )
}
