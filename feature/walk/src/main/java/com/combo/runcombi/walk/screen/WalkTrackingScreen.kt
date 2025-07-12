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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.walk.util.FormatUtils
import com.combo.runcombi.walk.viewmodel.WalkRecordViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import com.combo.runcombi.walk.component.FinishWalkBottomSheet

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalkTrackingScreen(
    onFinish: () -> Unit,
    walkRecordViewModel: WalkRecordViewModel = hiltViewModel(),
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

    val showSheet = remember { mutableStateOf(false) }

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
        Spacer(Modifier.height(32.dp))
        Text("함께 운동한 시간", color = Color.White)
        Spacer(Modifier.height(16.dp))
        TimeDisplayLarge(uiState.time)
        Spacer(Modifier.height(16.dp))
        DistanceDisplayLarge(uiState.distance)
        Spacer(Modifier.height(8.dp))
        CalorieDisplayLarge(uiState.calorie)
        Spacer(Modifier.height(64.dp))
        PauseButton(onClick = {
            if (!isPaused) {
                showSheet.value = true
                walkRecordViewModel.togglePause()
            }
        })
    }

    FinishWalkBottomSheet(
        show = showSheet.value,
        onDismiss = {
            showSheet.value = false
            walkRecordViewModel.togglePause()
        },
        onFinish = {
            showSheet.value = false
            onFinish()
        },
        onResume = {
            showSheet.value = false
            walkRecordViewModel.togglePause()
        }
    )
}

@Composable
fun TimeDisplay(time: Int) {
    val formattedTime = FormatUtils.formatTime(time)
    Text("운동 시간: $formattedTime", color = Color.White)
}

@Composable
fun DistanceDisplay(distance: Double) {
    val formattedDistance = FormatUtils.formatDistance(distance)
    Text("운동 거리: $formattedDistance km", color = Color.White)
}

@Composable
fun CalorieDisplay(calorie: Double) {
    val formattedCalorie = FormatUtils.formatCalorie(calorie)
    Text("칼로리 소모: $formattedCalorie kcal", color = Color.White)
}

@Composable
fun TimeDisplayLarge(time: Int) {
    val formattedTime = FormatUtils.formatTime(time)
    Text(
        text = formattedTime,
        color = Color.White,
        fontSize = 64.sp,
        modifier = Modifier
    )
}

@Composable
fun DistanceDisplayLarge(distance: Double) {
    val formattedDistance = FormatUtils.formatDistance(distance)
    Text(
        text = "$formattedDistance Km",
        color = Color(0xFFBDBDBD),
        fontSize = 28.sp,
        modifier = Modifier
    )
}

@Composable
fun CalorieDisplayLarge(calorie: Double) {
    val formattedCalorie = FormatUtils.formatCalorie(calorie)
    Text(
        text = "$formattedCalorie Kcal",
        color = Color.White,
        fontSize = 28.sp,
        modifier = Modifier
    )
}

@Composable
fun PauseButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(80.dp)
            .width(80.dp)
    ) {
        Text("II", fontSize = 32.sp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun PreviewWalkTrackingScreen() {
    WalkTrackingScreen(
        onFinish = {},
        walkRecordViewModel = viewModel()
    )
}
