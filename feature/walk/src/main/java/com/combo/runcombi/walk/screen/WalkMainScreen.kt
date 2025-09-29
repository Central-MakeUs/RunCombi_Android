import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.analytics.logScreenView
import com.combo.runcombi.core.designsystem.component.NetworkImage
import com.combo.runcombi.core.designsystem.component.RunCombiBottomSheet
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle4
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.feature.walk.R
import com.combo.runcombi.ui.ext.clickableSingle
import com.combo.runcombi.ui.ext.clickableWithoutRipple
import com.combo.runcombi.ui.ext.customPolygonClip
import com.combo.runcombi.walk.model.PetUiModel
import com.combo.runcombi.walk.model.WalkEvent
import com.combo.runcombi.walk.model.WalkMainUiState
import com.combo.runcombi.walk.util.GeoAddressUtil
import com.combo.runcombi.walk.util.LocationUtil
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
    walkMainViewModel: WalkMainViewModel,
    modifier: Modifier = Modifier,
    onStartWalk: () -> Unit = {},
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState()
    val uiState by walkMainViewModel.uiState.collectAsStateWithLifecycle()
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val notificationPermissionState =
        rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    var showPermissionSettingSheet by remember { mutableStateOf(false) }
    val analyticsHelper = walkMainViewModel.analyticsHelper

    LaunchedEffect(Unit) {
        analyticsHelper.logScreenView("WalkMainScreen")

        if (!locationPermissionState.status.isGranted) {
            locationPermissionState.launchPermissionRequest()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && notificationPermissionState != null && !notificationPermissionState.status.isGranted) {
            notificationPermissionState.launchPermissionRequest()
        }

        walkMainViewModel.fetchUserAndPets()
    }

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            val myLocation = LocationUtil.getCurrentLocation(context)
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
            val address = GeoAddressUtil.getAddress(context, it)
            walkMainViewModel.updateAddress(address)
        }
    }

    LaunchedEffect(Unit) {
        walkMainViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is WalkEvent.RequestLocationPermission -> {
                    showPermissionSettingSheet = true
                }

                else -> Unit
            }
        }
    }

    RunCombiBottomSheet(
        show = showPermissionSettingSheet,
        onDismiss = { showPermissionSettingSheet = false },
        onAccept = {
            showPermissionSettingSheet = false
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = ("package:" + context.packageName).toUri()
            }
            context.startActivity(intent)
        },
        onCancel = { showPermissionSettingSheet = false },
        title = "정확한 워치 권한 필요",
        subtitle = "운동을 시작하려면 정확한 위치 권한이 필요해요.\n설정에서 권한을 허용해주세요.",
        acceptButtonText = "설정으로 이동",
        cancelButtonText = "취소"
    )

    WalkMainContent(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        uiState = uiState,
        isLocationPermissionGranted = locationPermissionState.status.isGranted,
        onPetClick = { walkMainViewModel.togglePetSelect(it) },
        onStartWalk = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !notificationPermissionState.status.isGranted) {
                notificationPermissionState.launchPermissionRequest()
            }

            if (!locationPermissionState.status.isGranted) {
                if (locationPermissionState.status.shouldShowRationale) {
                    locationPermissionState.launchPermissionRequest()
                } else {
                    walkMainViewModel.onStartWalkClicked(false)
                }
                return@WalkMainContent
            }

            if (walkMainViewModel.checkWithInitWalkData()) {
                onStartWalk()
            } else {
                Toast.makeText(context, "함께 운동할 콤비를 선택해주세요.", Toast.LENGTH_SHORT).show()
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
    showBottomSheet: Boolean = false,
    bottomSheetContent: @Composable (() -> Unit)? = null,
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

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

        if (isLandscape) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 72.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LocationAddressLabel(
                    address = if (isLocationPermissionGranted) uiState.address else "위치 접근 미허용",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        CombiList(
                            member = uiState.member,
                            petUiList = uiState.petUiList,
                            onPetClick = onPetClick
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        StartWalkButton(
                            modifier = Modifier.size(120.dp),
                            onClick = onStartWalk
                        )
                    }
                }
                if (showBottomSheet && bottomSheetContent != null) {
                    bottomSheetContent()
                }
            }
        } else {
            Column(
                modifier = Modifier.padding(top = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LocationAddressLabel(
                    address = if (isLocationPermissionGranted) uiState.address else "위치 접근 미허용",
                )
                Spacer(modifier = Modifier.height(46.dp))
                CombiList(
                    member = uiState.member,
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
            .clickableSingle(onClick = onClick), contentAlignment = Alignment.Center
    ) {
        Text(
            text = "운동", style = giantsTitle2, color = Grey02
        )
    }
}

@Composable
private fun MemberProfile(member: Member) {
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

        NetworkImage(
            contentScale = ContentScale.Crop,
            imageUrl = member.profileImageUrl,
            drawableResId = R.drawable.person_profile,
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .padding(end = 12.dp)
                .customPolygonClip(
                    bottomLeft = true,
                    topRight = true,
                    polygonSize = 16.dp,
                    bottomLeftAngle = 62.0
                )
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
        val startPadding = 12.dp
        val endPadding = if (isCenter) 12.dp else 0.dp

        Box(
            modifier = Modifier
                .height(boxHeight)
                .width(boxWidth)
                .clickableWithoutRipple { onClick?.invoke() },
            contentAlignment = Alignment.Center
        ) {
            StableImage(
                drawableResId = boxImage,
                modifier = Modifier
                    .height(boxHeight)
                    .width(boxWidth),
            )
            NetworkImage(
                contentScale = ContentScale.Crop,
                imageUrl = pet.profileImageUrl,
                drawableResId = R.drawable.ic_pet_defalut,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = startPadding, end = endPadding)
                    .padding(6.dp)
                    .customPolygonClip(
                        bottomLeft = true,
                        topRight = true,
                        polygonSize = 16.dp,
                        bottomLeftAngle = 58.0,
                        topRightAngle = 63.0
                    )
            )
        }
    }
}

@Composable
private fun CombiList(
    member: Member?,
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
            member?.let {
                MemberProfile(member = it)
            }
            allPets.forEachIndexed { index, petUi ->
                PetProfile(
                    pet = petUi.pet,
                    isSelected = petUi.isSelected,
                    isCenter = allPets.size > 1 && index == 0,
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
        uiState = WalkMainUiState(
            member = Member(
                nickname = "name",
                gender = Gender.MALE,
                height = 100,
                weight = 100,
                profileImageUrl = null
            ),
            petUiList = listOf(
                PetUiModel(
                    pet = Pet(
                        id = 0,
                        name = "초코",
                        weight = 10.0,
                        age = 10,
                        profileImageUrl = null,
                        runStyle = RunStyle.WALKING
                    ),
                    isSelected = true,
                    originIndex = 0,
                ),
                PetUiModel(
                    pet = Pet(
                        id = 1,
                        name = "초코",
                        weight = 10.0,
                        age = 10,
                        profileImageUrl = null,
                        runStyle = RunStyle.WALKING
                    ),
                    isSelected = false,
                    originIndex = 1,
                )
            )
        ),
        isLocationPermissionGranted = true,
        onPetClick = {},
        onStartWalk = {}
    )
}
