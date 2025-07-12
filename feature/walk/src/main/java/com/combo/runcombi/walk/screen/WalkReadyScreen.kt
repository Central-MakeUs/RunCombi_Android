package com.combo.runcombi.walk.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsHeading1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle3
import com.combo.runcombi.walk.viewmodel.WalkReadyViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import android.os.Build
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun WalkReadyScreen(
    onBack: () -> Unit,
    onCompleteReady: () -> Unit,
    viewModel: WalkReadyViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val permissions = buildList {
        add(Manifest.permission.ACCESS_FINE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
    val permissionsState = rememberMultiplePermissionsState(permissions)
    var showPermissionDialog by remember { mutableStateOf(false) }
    val event by viewModel.event.collectAsState()

    LaunchedEffect(Unit) {
        if (permissionsState.permissions.any { !it.status.isGranted }) {
            permissionsState.launchMultiplePermissionRequest()
        }
    }

    LaunchedEffect(event) {
        when (event) {
            WalkReadyViewModel.Event.RequestLocationPermission -> {
                showPermissionDialog = true
                viewModel.consumeEvent()
            }
            else -> Unit
        }
    }

    val locationPermission = permissionsState.permissions.find { it.permission == Manifest.permission.ACCESS_FINE_LOCATION }
    val notificationPermission = permissionsState.permissions.find { it.permission == Manifest.permission.POST_NOTIFICATIONS }

    val locationDeniedPermanently = locationPermission != null &&
        !locationPermission.status.isGranted &&
        !locationPermission.status.shouldShowRationale

    val notificationDeniedPermanently = notificationPermission != null &&
        !notificationPermission.status.isGranted &&
        !notificationPermission.status.shouldShowRationale

    val anyDeniedPermanently = locationDeniedPermanently || notificationDeniedPermanently

    Column(modifier = Modifier.background(color = Grey01)) {
        RunCombiAppTopBar(
            isVisibleBackBtn = true, onBack = onBack
        )
        Spacer(modifier = Modifier.height(50.dp))
        WalkReadyContent(showPermissionDialog = showPermissionDialog, onCompleteReady = {
            if (permissionsState.permissions.any { !it.status.isGranted }) {
                if (anyDeniedPermanently) {
                    showPermissionDialog = true
                } else {
                    permissionsState.launchMultiplePermissionRequest()
                }
            } else {
                onCompleteReady()
            }
        }, onGoToSetting = {
            showPermissionDialog = false
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = ("package:" + context.packageName).toUri()
            }
            context.startActivity(intent)
        }, onDismissDialog = { showPermissionDialog = false },
        anyDeniedPermanently = anyDeniedPermanently)
    }
}

@Composable
private fun WalkReadyContent(
    showPermissionDialog: Boolean,
    onCompleteReady: () -> Unit,
    onGoToSetting: () -> Unit,
    onDismissDialog: () -> Unit,
    anyDeniedPermanently: Boolean = false,
) {
    if (showPermissionDialog) {
        AlertDialog(
            onDismissRequest = onDismissDialog,
            title = { Text("권한 필요") },
            text = {
                if (anyDeniedPermanently) {
                    Text("위치, 알림 권한 두 권한을 모두 허용해 주세요.")
                } else {
                    Text("운동을 시작하려면 위치 권한과 알림 권한이 모두 필요합니다.\n\n설정에서 두 권한을 모두 허용해 주세요.")
                }
            },
            confirmButton = {
                Text(
                    "설정으로 이동", modifier = Modifier.clickable { onGoToSetting() })
            },
            dismissButton = {
                Text(
                    "닫기", modifier = Modifier.clickable { onDismissDialog() })
            })
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("함께 운동한 시간", style = giantsTitle3, color = Grey08)
            Spacer(modifier = Modifier.height(10.dp))
            Text("00:00", style = giantsHeading1, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Primary01, shape = RoundedCornerShape(4.dp))
                    .clickable { onCompleteReady() }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "시작", style = RunCombiTypography.giantsTitle2, color = Grey01
                )
            }

            Spacer(modifier = Modifier.height(130.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WalkReadyContentPreview() {
    WalkReadyContent(
        showPermissionDialog = false,
        onCompleteReady = {},
        onGoToSetting = {},
        onDismissDialog = {},
        anyDeniedPermanently = false)
} 