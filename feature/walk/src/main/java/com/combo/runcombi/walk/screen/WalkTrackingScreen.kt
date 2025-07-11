package com.combo.runcombi.walk.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.walk.LocationProvider
import com.combo.runcombi.walk.viewmodel.WalkRecordViewModel
import kotlinx.coroutines.delay
import com.google.android.gms.maps.model.LatLng


@Composable
fun WalkTrackingScreen(
    onFinish: () -> Unit,
    walkRecordViewModel: WalkRecordViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
) {
    val context = LocalContext.current
    val uiModel by walkRecordViewModel.uiModel.collectAsState()
    var isWalking by remember { mutableStateOf(true) }
    var isPaused by remember { mutableStateOf(false) }
    var lastLocation by remember { mutableStateOf<com.google.android.gms.maps.model.LatLng?>(null) }
    var lastUpdateTime by remember { mutableStateOf(System.currentTimeMillis()) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(isWalking, isPaused) {
        if (isWalking && !isPaused) {
            LocationProvider.startLocationUpdates(context) { location ->
                val now = System.currentTimeMillis()
                val timeDeltaSec = ((now - lastUpdateTime) / 1000).coerceAtLeast(1)
                walkRecordViewModel.addPathPoint(location, timeDeltaSec)
                lastUpdateTime = now
            }
        }
        onDispose {
            LocationProvider.stopLocationUpdates()
        }
    }

    LaunchedEffect(isWalking, isPaused) {
        while (isWalking && !isPaused) {
            walkRecordViewModel.updateTime(uiModel.time + 1)
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("운동 시간: ${uiModel.time}s", color = Color.White)
        Text("운동 거리: ${uiModel.distance} m", color = Color.White)
        Text("평균 속도: ${uiModel.speed} m/s", color = Color.White)
        Text("칼로리 소모: ${uiModel.calorie} kcal", color = Color.White)
        Spacer(Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            if (!isPaused) {
                Button(onClick = { isPaused = true }) { Text("일시정지", color = Color.White) }
            } else {
                Button(onClick = { isPaused = false }) { Text("재시작", color = Color.White) }
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = {
                isWalking = false
                onFinish()
            }) { Text("운동 끝내기") }
        }
    }
} 
