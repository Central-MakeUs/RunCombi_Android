package com.combo.runcombi.walk.screen

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Primary02
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2
import com.combo.runcombi.feature.walk.R
import com.combo.runcombi.walk.AddressResolver
import com.combo.runcombi.walk.LocationProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState


@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun WalkMainScreen(
    modifier: Modifier = Modifier,
    walkMainViewModel: WalkMainViewModel = hiltViewModel(),
    onStartWalk: () -> Unit = {},
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val uiState by walkMainViewModel.uiState.collectAsState()
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }
    }
    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            val myLocation = LocationProvider.getCurrentLocation(context)
            myLocation?.let { walkMainViewModel.updateLocation(it) }
        } else {
            cameraPositionState.move(
                newLatLngZoom(
                    com.google.android.gms.maps.model.LatLng(
                        36.5,
                        127.8
                    ), 7f
                )
            )
            walkMainViewModel.updateAddress("위치 접근 미허용")
        }
    }

    LaunchedEffect(uiState.myLocation) {
        uiState.myLocation?.let {
            cameraPositionState.move(newLatLngZoom(it, 16f))
            val address = AddressResolver.getAddress(context, it)
            walkMainViewModel.updateAddress(address)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = locationPermissionState.status.isGranted,
                mapType = MapType.NORMAL,
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                    LocalContext.current, R.raw.google_map_dark_theme_style
                )
            ),
            uiSettings = MapUiSettings(
                zoomGesturesEnabled = false,
                zoomControlsEnabled = false,
                compassEnabled = false,
                myLocationButtonEnabled = false,
                scrollGesturesEnabled = false,
                tiltGesturesEnabled = false
            )
        )

        LocationAddressLabel(
            address = if (locationPermissionState.status.isGranted) uiState.address else "위치 접근 미허용",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 15.dp)
        )

        StartWalkButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp),
            onClick = onStartWalk
        )
    }
}

@Composable
private fun LocationAddressLabel(
    address: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        StableImage(drawableResId = R.drawable.ic_gps, modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(10.dp))
        Text(address, color = Grey06, style = body3)
    }
}


@Composable
private fun StartWalkButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .background(Primary02)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "운동", style = title2, color = Grey02
        )
    }
}


@Preview(showBackground = true)
@Composable
fun WalkMainScreenPreview() {
    WalkMainScreen()
}
