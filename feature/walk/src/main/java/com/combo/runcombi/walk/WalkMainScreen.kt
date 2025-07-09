package com.combo.runcombi.walk

import android.Manifest
import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
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
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Primary02
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2
import com.combo.runcombi.feature.walk.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale


@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun WalkMainScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val cameraPositionState = rememberCameraPositionState()
    var myLocation by remember { mutableStateOf<LatLng?>(null) }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).addOnSuccessListener { location ->
                location?.let {
                    val latLng = LatLng(it.latitude, it.longitude)
                    myLocation = latLng
                    cameraPositionState.move(newLatLngZoom(latLng, 16f))
                }
            }
        } else {
            locationPermissionState.launchPermissionRequest()
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
                    LocalContext.current, R.raw.gogle_map_dark_theme_style
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
            latLng = myLocation, modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
        )

        StartWalkButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp)
        )
    }
}

@Composable
private fun LocationAddressLabel(
    latLng: LatLng?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var address by remember { mutableStateOf("주소 불러오는 중...") }

    LaunchedEffect(latLng) {
        if (latLng != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val geocoder = Geocoder(context, Locale.getDefault())
                geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1,
                    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            if (addresses.isEmpty()) {
                                address = ""
                            } else {
                                address = listOfNotNull(
                                    addresses[0].adminArea?.takeIf { it.isNotBlank() },
                                    addresses[0].locality?.takeIf { it.isNotBlank() },
                                    addresses[0].subLocality?.takeIf { it.isNotBlank() }
                                ).joinToString(" ")
                            }
                        }

                        override fun onError(errorMessage: String?) {
                            address = ""
                        }
                    }
                )
            } else {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addresses = withContext(Dispatchers.IO) {
                        geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    }
                    address = if (addresses.isNullOrEmpty()) {
                        "주소 정보 없음"
                    } else {
                        listOfNotNull(
                            addresses[0].adminArea?.takeIf { it.isNotBlank() },
                            addresses[0].locality?.takeIf { it.isNotBlank() },
                            addresses[0].subLocality?.takeIf { it.isNotBlank() }
                        ).joinToString(" ")
                    }
                } catch (e: Exception) {
                    address = "주소 불러오기 실패"
                }
            }
        } else {
            address = "위치 정보 없음"
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.Place, contentDescription = null, tint = Color.White)
        Spacer(Modifier.width(4.dp))
        Text(address, color = Color.White)
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
