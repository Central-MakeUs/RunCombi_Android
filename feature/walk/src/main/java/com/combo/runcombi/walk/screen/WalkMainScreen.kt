package com.combo.runcombi.walk.screen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle4
import com.combo.runcombi.domain.user.model.User
import com.combo.runcombi.feature.walk.R
import com.combo.runcombi.pet.model.Pet
import com.combo.runcombi.walk.AddressResolver
import com.combo.runcombi.walk.LocationProvider
import com.combo.runcombi.walk.component.LocationPermissionDialog
import com.combo.runcombi.walk.model.PetUiModel
import com.combo.runcombi.walk.model.WalkEvent
import com.combo.runcombi.walk.model.WalkMainUiState
import com.combo.runcombi.walk.viewmodel.WalkMainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.flow.collectLatest


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
    var showPermissionDialog by remember { mutableStateOf(false) }

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
                    LatLng(
                        36.5, 127.8
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

    LaunchedEffect(Unit) {
        walkMainViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is WalkEvent.RequestLocationPermission -> {
                    showPermissionDialog = true
                }

                else -> Unit
            }
        }
    }

    LocationPermissionDialog(
        show = showPermissionDialog,
        onDismiss = { showPermissionDialog = false },
        onConfirm = {
            showPermissionDialog = false
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = ("package:" + context.packageName).toUri()
            }
            context.startActivity(intent)
        })

    WalkMainContent(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiState = uiState,
        isLocationPermissionGranted = locationPermissionState.status.isGranted,
        onPetClick = { walkMainViewModel.togglePetSelect(it) },
        onStartWalk = {
            if (!locationPermissionState.status.isGranted) {
                if (locationPermissionState.status.shouldShowRationale) {
                    locationPermissionState.launchPermissionRequest()
                } else {
                    walkMainViewModel.onStartWalkClicked(false)
                }
            } else if (uiState.petUiList.none { it.isSelected }) {
                Toast.makeText(context, "함께 운동할 콤비를 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                onStartWalk()
            }
        }
    )
}

@Composable
fun WalkMainContent(
    modifier: Modifier = Modifier,
    cameraPositionState: CameraPositionState,
    uiState: WalkMainUiState,
    isLocationPermissionGranted: Boolean = false,
    onPetClick: (Pet) -> Unit = {},
    onStartWalk: () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = isLocationPermissionGranted,
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
        )

        Column(
            modifier = Modifier.padding(top = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LocationAddressLabel(
                address = if (isLocationPermissionGranted) uiState.address else "위치 접근 미허용",
            )
            Spacer(modifier = Modifier.height(46.dp))
            CombiList(
                user = uiState.user,
                petUiList = uiState.petUiList,
                onPetClick = onPetClick
            )
        }

        StartWalkButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 130.dp),
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
            .background(Primary01, shape = RoundedCornerShape(4.dp))
            .clickable(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "운동", style = giantsTitle2, color = Grey02
        )
    }
}

@Composable
private fun UserProfile(user: User) {
    Box(
        modifier = Modifier
            .height(77.dp)
            .width(84.dp), contentAlignment = Alignment.Center
    ) {
        StableImage(
            drawableResId = R.drawable.ic_user_box,
            modifier = Modifier
                .height(77.dp)
                .width(84.dp),
        )

        StableImage(
            drawableResId = R.drawable.person_profile,
            modifier = Modifier
                .size(50.dp)
                .padding(end = 10.dp)
        )
    }
}

@Composable
private fun PetProfile(
    pet: Pet,
    isSelected: Boolean,
    isCenter: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val boxImage = when {
            isCenter && isSelected -> R.drawable.ic_pet_box_center_selected
            isCenter && !isSelected -> R.drawable.ic_pet_box_center
            isSelected -> R.drawable.ic_pet_box_selected
            else -> R.drawable.ic_pet_box
        }

        val boxHeight = if (isCenter) 77.dp else 77.dp
        val boxWidth = if (isCenter) 99.dp else 85.dp
        val startPadding = if (isCenter) 0.dp else 10.dp

        Box(
            modifier = Modifier
                .height(boxHeight)
                .width(boxWidth)
                .clickable(enabled = onClick != null) { onClick?.invoke() },
            contentAlignment = Alignment.Center
        ) {
            StableImage(
                drawableResId = boxImage,
                modifier = Modifier
                    .height(boxHeight)
                    .width(boxWidth),
            )
            StableImage(
                drawableResId = R.drawable.ic_pet_defalut,
                modifier = Modifier
                    .height(31.dp)
                    .width(52.dp)
                    .padding(start = startPadding),
            )

        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            pet.name,
            style = body1,
            color = Grey06,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = startPadding),
        )
    }
}

@Composable
private fun CombiList(
    user: User?,
    petUiList: List<PetUiModel>,
    onPetClick: (Pet) -> Unit,
) {
    val selected = petUiList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }
    val unselected = petUiList.filter { !it.isSelected }.sortedBy { it.originIndex }
    val allPets = selected + unselected

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("함께 운동할 콤비", style = giantsTitle4, color = Grey08)
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            user?.let {
                UserProfile(user = it)
            }
            allPets.forEachIndexed { index, petUi ->
                PetProfile(
                    pet = petUi.pet,
                    isSelected = petUi.isSelected,
                    isCenter = index == 0,
                    onClick = { onPetClick(petUi.pet) }
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        if (selected.isNotEmpty()) {
            val selectedCombis = selected.map { it.pet.name }.joinToString()
            Text(
                "${selectedCombis}와 함께!",
                style = body1,
                color = Primary01,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WalkMainContentPreview() {
    val cameraPositionState = rememberCameraPositionState()
    WalkMainContent(
        cameraPositionState = cameraPositionState,
        uiState = WalkMainUiState(),
        isLocationPermissionGranted = true,
        onPetClick = {},
        onStartWalk = {}
    )
}
