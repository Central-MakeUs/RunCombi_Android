package com.combo.runcombi.walk.screen

import android.annotation.SuppressLint
import android.os.Looper
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.walk.viewmodel.WalkRecordViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay

private const val ACTION_WALK_LOCATION = "ACTION_WALK_LOCATION"

@SuppressLint("MissingPermission")
@Composable
fun WalkTrackingScreen(
    onFinish: () -> Unit,
    walkRecordViewModel: WalkRecordViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by walkRecordViewModel.uiState.collectAsState()
    val isPaused = uiState.isPaused

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("운동 시간: ${uiState.time}s", color = Color.White)
        Text("운동 거리: ${uiState.distance} m", color = Color.White)
        Text("평균 속도: ${uiState.speed} m/s", color = Color.White)
        Text("칼로리 소모: ${uiState.calorie} kcal", color = Color.White)
        Spacer(Modifier.height(32.dp))
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { walkRecordViewModel.togglePause() }
            ) {
                Text(if (isPaused) "재시작" else "일시정지")
            }
            Spacer(Modifier.width(16.dp))
            Button(onClick = onFinish) {
                Text("운동 종료")
            }
        }
    }
} 
