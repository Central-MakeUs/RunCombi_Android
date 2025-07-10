package com.combo.runcombi.walk.screen

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.combo.runcombi.walk.LocationProvider
import com.combo.runcombi.walk.viewmodel.WalkRecordViewModel
import kotlinx.coroutines.delay


@Composable
fun WalkTrackingScreen(
    onFinish: () -> Unit,
    walkRecordViewModel: WalkRecordViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiModel by walkRecordViewModel.uiModel.collectAsState()
    var isWalking by remember { mutableStateOf(true) }
    var isPaused by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                isPaused = true
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(isWalking, isPaused) {
        while (isWalking && !isPaused) {
            val location = LocationProvider.getCurrentLocation(context)
            if (location != null) {
                walkRecordViewModel.addPathPoint(location)
                val speed = if (uiModel.time > 0) uiModel.distance / uiModel.time else 0.0
                walkRecordViewModel.updateSpeed(speed)
                walkRecordViewModel.updateCalorie(uiModel.distance * 0.05)
            }
            walkRecordViewModel.updateTime(uiModel.time + 1)
            delay(1000)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("운동 시간: ${uiModel.time}s")
        Text("운동 거리: ${uiModel.distance} m")
        Text("평균 속도: ${uiModel.speed} m/s")
        Spacer(Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            if (!isPaused) {
                Button(onClick = { isPaused = true }) { Text("일시정지") }
            } else {
                Button(onClick = { isPaused = false }) { Text("재시작") }
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = {
                isWalking = false
                onFinish()
            }) { Text("운동 끝내기") }
        }
    }
} 