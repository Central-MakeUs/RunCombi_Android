package com.combo.runcombi.walk.screen

import android.annotation.SuppressLint
import android.os.Looper
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary02
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography
import com.combo.runcombi.feature.walk.R
import com.combo.runcombi.walk.component.WalkBottomSheet
import com.combo.runcombi.walk.model.BottomSheetType
import com.combo.runcombi.walk.model.WalkTrackingEvent
import com.combo.runcombi.walk.model.WalkUiState
import com.combo.runcombi.walk.model.getBottomSheetContent
import com.combo.runcombi.walk.util.FormatUtils
import com.combo.runcombi.walk.viewmodel.WalkRecordViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.tooling.preview.Preview
import android.content.Intent
import com.combo.runcombi.walk.WalkTrackingService
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import com.combo.runcombi.walk.WalkLocationBroadcastReceiver

@SuppressLint("MissingPermission")
@Composable
fun WalkTrackingScreen(
    onFinish: () -> Unit,
    onBack: () -> Unit,
    walkRecordViewModel: WalkRecordViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by walkRecordViewModel.uiState.collectAsState()
    val isPaused = uiState.isPaused
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val serviceIntent = remember { Intent(context, WalkTrackingService::class.java) }
    val serviceStarted = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (!serviceStarted.value) {
            context.startForegroundService(serviceIntent)
            serviceStarted.value = true
        }
    }

    val showSheet = remember { mutableStateOf(BottomSheetType.NONE) }
    LaunchedEffect(showSheet.value) {
        if (showSheet.value == BottomSheetType.FINISH || showSheet.value == BottomSheetType.CANCEL) {
            context.stopService(serviceIntent)
            serviceStarted.value = false
        }
    }

    DisposableEffect(Unit) {
        val receiver = WalkLocationBroadcastReceiver(
            isPausedProvider = { isPaused },
            onLocationReceived = walkRecordViewModel::addPathPointFromService
        )
        val filter = IntentFilter(WalkLocationBroadcastReceiver.ACTION_BROADCAST_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            ContextCompat.registerReceiver(
                context,
                receiver,
                filter,
                ContextCompat.RECEIVER_EXPORTED
            )
        }
        onDispose {
            context.unregisterReceiver(receiver)
            context.stopService(serviceIntent)
        }
    }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    walkRecordViewModel.addPathPointFromService(
                        location.latitude,
                        location.longitude,
                        location.accuracy,
                        location.time
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        walkRecordViewModel.eventFlow.collectLatest { event ->
            if (event is WalkTrackingEvent.ShowBottomSheet) {
                showSheet.value = event.type
            }
        }
    }

    DisposableEffect(isPaused) {
        if (!isPaused) {
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
                .setMinUpdateIntervalMillis(1000)
                .build()

            fusedLocationClient.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )
        }
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    LaunchedEffect(isPaused) {
        while (!isPaused) {
            walkRecordViewModel.updateTime(uiState.time + 1)
            delay(1000)
        }
    }

    WalkTrackingContent(
        uiState = uiState,
        onPauseToggle = walkRecordViewModel::togglePause,
        onCancelClick = { walkRecordViewModel.emitShowBottomSheet(BottomSheetType.CANCEL) },
        onFinishClick = { walkRecordViewModel.emitShowBottomSheet(BottomSheetType.FINISH) }
    )

    val content = getBottomSheetContent(showSheet.value)
    content?.let {
        WalkBottomSheet(
            show = true,
            onDismiss = { showSheet.value = BottomSheetType.NONE },
            onAccept = {
                when (showSheet.value) {
                    BottomSheetType.FINISH -> onFinish()
                    BottomSheetType.CANCEL -> onBack()
                    else -> Unit
                }
                showSheet.value = BottomSheetType.NONE
            },
            onCancel = { showSheet.value = BottomSheetType.NONE },
            title = content.title,
            subtitle = content.subtitle,
            acceptButtonText = content.acceptButtonText,
            cancelButtonText = content.cancelButtonText
        )
    }
}

@Composable
fun WalkTrackingContent(
    uiState: WalkUiState,
    onPauseToggle: () -> Unit,
    onCancelClick: () -> Unit,
    onFinishClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 20.dp, vertical = 10.dp),
        ) {
            Text(
                "취소",
                style = RunCombiTypography.title3,
                color = Grey05,
                modifier = Modifier.clickable { onCancelClick() })
            Spacer(modifier = Modifier.weight(1f))
            Text(
                "완료",
                style = RunCombiTypography.title3,
                color = Primary02,
                modifier = Modifier.clickable { onFinishClick() })
        }
        Spacer(Modifier.height(22.dp))
        Text("함께 운동한 시간", color = Grey08, style = RunCombiTypography.giantsTitle3)
        Spacer(Modifier.height(10.45.dp))
        TimeDisplayLarge(uiState.time)
        Spacer(Modifier.height(26.55.dp))
        DistanceDisplayLarge(uiState.distance)
        Spacer(Modifier.weight(1f))
        PauseButton(onClick = onPauseToggle, isPaused = uiState.isPaused)
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun TimeDisplayLarge(time: Int) {
    val formattedTime = FormatUtils.formatTime(time)
    Text(
        text = formattedTime,
        color = Color.White,
        style = RunCombiTypography.giantsHeading1
    )
}

@Composable
fun DistanceDisplayLarge(distance: Double) {
    val formattedDistance = FormatUtils.formatDistance(distance)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = formattedDistance,
            color = Grey06,
            style = RunCombiTypography.giantsTitle2,
            modifier = Modifier.alignByBaseline()
        )
        Text(
            text = " Km",
            color = Grey06,
            style = RunCombiTypography.giantsTitle6,
            modifier = Modifier.alignByBaseline()
        )
    }
}

@Composable
fun CalorieDisplayLarge(calorie: Double) {
    val formattedCalorie = FormatUtils.formatCalorie(calorie)
    Text(
        text = "$formattedCalorie Kcal",
        color = Color.White,
        fontSize = 28.sp
    )
}

@Composable
fun PauseButton(onClick: () -> Unit, isPaused: Boolean) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(color = Grey02, shape = RoundedCornerShape(4.dp))
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        StableImage(
            drawableResId = if (isPaused) R.drawable.ic_resume else R.drawable.ic_pause,
            modifier = Modifier
                .width(if (isPaused) 32.dp else 17.8.dp)
                .height(if (isPaused) 32.dp else 24.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun WalkTrackingContentPreview() {
    WalkTrackingContent(
        uiState = WalkUiState(
            time = 1234,
            distance = 2.5,
            calorie = 120.0,
            isPaused = false
        ),
        onPauseToggle = {},
        onCancelClick = {},
        onFinishClick = {}
    )
}