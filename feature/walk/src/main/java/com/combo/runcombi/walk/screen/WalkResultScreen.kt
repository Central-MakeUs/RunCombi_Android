package com.combo.runcombi.walk.screen

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.LottieImage
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey07
import com.combo.runcombi.core.designsystem.theme.Primary02
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle5
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle6
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.feature.walk.R
import com.combo.runcombi.ui.ext.clickableSingle
import com.combo.runcombi.ui.util.BitmapUtil
import com.combo.runcombi.ui.util.FormatUtils
import com.combo.runcombi.walk.model.WalkResultEvent
import com.combo.runcombi.walk.viewmodel.WalkMainViewModel
import com.combo.runcombi.walk.viewmodel.WalkResultViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

fun createCircleMarkerBitmap(color: Int, size: Int = 42): Bitmap {
    val bitmap = createBitmap(size, size)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply {
        isAntiAlias = true
        this.color = color
    }
    canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
    return bitmap
}

@Composable
fun WalkResultScreen(
    walkMainViewModel: WalkMainViewModel = hiltViewModel(),
    walkResultViewModel: WalkResultViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNavigateToRecord: (Int) -> Unit = {},
) {
    val context = LocalContext.current
    var cameraImageFile by remember { mutableStateOf<File?>(null) }
    var captureImageFile by remember { mutableStateOf<File?>(null) }
    val showCaptureRequest = remember { mutableStateOf(false) }
    val walkData by walkMainViewModel.walkData.collectAsStateWithLifecycle()
    val startRunData = walkData.runData
    val formattedTime = FormatUtils.formatMinute(walkData.time)
    val formattedDistance = FormatUtils.formatDistance(walkData.distance)
    val isLoading by walkResultViewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessageFlow = walkResultViewModel.errorMessage
    val coroutineScope = rememberCoroutineScope()
    var hasSavedRun by remember { mutableStateOf(false) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            if (bitmap != null) {
                val file = BitmapUtil.bitmapToFile(
                    context,
                    bitmap,
                    "camera_${System.currentTimeMillis()}.jpg"
                )
                cameraImageFile = file
                walkData.runData?.runId?.let { runId ->
                    walkResultViewModel.setRunImage(runId, file)
                }
            } else {
                cameraImageFile = null
                onNavigateToRecord(walkData.runData?.runId ?: 0)
            }
        }

    val cameraPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                walkResultViewModel.openCamera()
            } else {
                onNavigateToRecord(walkData.runData?.runId ?: 0)
            }
        }

    fun handleCameraButtonClick() {
        val permission = Manifest.permission.CAMERA
        val permissionState = ContextCompat.checkSelfPermission(context, permission)
        if (permissionState == PackageManager.PERMISSION_GRANTED) {
            walkResultViewModel.openCamera()
        } else {
            cameraPermissionLauncher.launch(permission)
        }
    }

    LaunchedEffect(Unit) {
        errorMessageFlow.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        walkResultViewModel.eventFlow.collect { event ->
            when (event) {
                is WalkResultEvent.OpenCamera -> {
                    cameraLauncher.launch(null)
                }

                is WalkResultEvent.SetRunImageSuccess -> {
                    onNavigateToRecord(walkData.runData?.runId ?: 0)
                }

                is WalkResultEvent.SaveRunError -> {
                    onBack()
                }
            }
        }
    }

    LaunchedEffect(walkData.pathPoints) {
        if (!hasSavedRun && walkData.pathPoints.isNotEmpty()) {
            hasSavedRun = true
            delay(300)
            showCaptureRequest.value = true
        }
    }

    WalkResultContent(
        timeText = formattedTime,
        distanceText = formattedDistance,
        pathPoints = walkData.pathPoints,
        isFirstRun = startRunData?.isFirstRun == "Y",
        nthRun = startRunData?.nthRun ?: 0,
        onBack = onBack,
        showCaptureRequest = showCaptureRequest.value,
        onCaptured = { bitmap ->
            val file = BitmapUtil.bitmapToFile(
                context,
                bitmap,
                "walk_map_${System.currentTimeMillis()}.jpg"
            )
            captureImageFile = file
            showCaptureRequest.value = false
            coroutineScope.launch {
                walkResultViewModel.saveRun(
                    walkData = walkData,
                    routeImage = file
                )
            }
        },
        onClickCamera = {
            handleCameraButtonClick()
        }
    )

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary02)
        }
    }
}

@Composable
fun WalkResultContent(
    timeText: String,
    distanceText: String,
    isFirstRun: Boolean,
    nthRun: Int,
    pathPoints: List<LatLng>,
    onBack: () -> Unit = {},
    showCaptureRequest: Boolean = false,
    onCaptured: (Bitmap) -> Unit = {},
    onClickCamera: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Grey01),
            horizontalAlignment = CenterHorizontally
        ) {
            RunCombiAppTopBar(isVisibleBackBtn = false, isVisibleCloseBtn = true, onClose = onBack)
            Spacer(modifier = Modifier.height(22.dp))
            TitleSection(isFirstRun = isFirstRun, nthRun = nthRun)
            Spacer(modifier = Modifier.height(16.dp))
            MapViewContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(264.dp),
                pathPoints = pathPoints,
                mapStyleResId = R.raw.google_map_dark_theme_style,
                captureRequest = showCaptureRequest,
                onCaptured = { bitmap ->
                    onCaptured(bitmap)
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            StatInfoSection(timeText = timeText, distanceText = distanceText)
            Spacer(modifier = Modifier.weight(1f))
            CameraButton(onClick = {
                onClickCamera()
            })
            Spacer(modifier = Modifier.height(44.dp))
        }
        CelebrationEffect()
    }
}

@Composable
fun MapViewContainer(
    modifier: Modifier = Modifier,
    pathPoints: List<LatLng>,
    mapStyleResId: Int? = null,
    captureRequest: Boolean = false,
    onCaptured: (Bitmap) -> Unit = {},
) {
    val context = LocalContext.current
    val primary01Int = 0xFFD7FE63.toInt()
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            MapView(ctx).apply {
                onCreate(null)
                onResume()
                getMapAsync { googleMap ->
                    googleMap.uiSettings.apply {
                        isCompassEnabled = false
                        isZoomControlsEnabled = false
                        isMyLocationButtonEnabled = false
                        isMapToolbarEnabled = false
                        isIndoorLevelPickerEnabled = false
                        isScrollGesturesEnabled = false
                        isTiltGesturesEnabled = false
                        isRotateGesturesEnabled = false
                        isZoomGesturesEnabled = false
                    }
                    mapStyleResId?.let {
                        try {
                            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, it))
                        } catch (_: Exception) {
                        }
                    }
                    if (pathPoints.isNotEmpty()) {
                        val polylineOptions = PolylineOptions()
                            .addAll(pathPoints)
                            .color(primary01Int)
                            .width(10f)
                        googleMap.addPolyline(polylineOptions)
                        val whiteMarker =
                            BitmapDescriptorFactory.fromBitmap(createCircleMarkerBitmap(android.graphics.Color.WHITE))
                        googleMap.addMarker(
                            MarkerOptions().position(pathPoints.first()).icon(whiteMarker)
                        )
                        if (pathPoints.size > 1) {
                            googleMap.addMarker(
                                MarkerOptions().position(pathPoints.last()).icon(whiteMarker)
                            )
                        }
                        googleMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                pathPoints.first(), 16f
                            )
                        )
                    }
                }
            }
        },
        update = { mapView ->
            if (captureRequest) {
                mapView.getMapAsync { googleMap ->
                    googleMap.snapshot { bitmap ->
                        if (bitmap != null) onCaptured(bitmap)
                    }
                }
            }
        }
    )
}

@Composable
fun StatInfoSection(timeText: String, distanceText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 70.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = CenterHorizontally) {
            Text("운동 시간", color = Grey06, style = body2)
            Row {
                Text(
                    timeText,
                    color = Grey07,
                    style = giantsTitle2,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .alignByBaseline()
                        .padding(end = 8.dp)
                )
                Text(
                    " min",
                    color = Grey07,
                    style = giantsTitle6,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }
        Column(horizontalAlignment = CenterHorizontally) {
            Text("운동 거리", color = Grey06, style = body2)
            Row {
                Text(
                    distanceText,
                    color = Grey07,
                    style = giantsTitle2,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .alignByBaseline()
                        .padding(end = 8.dp)
                )
                Text(
                    " km",
                    color = Grey07,
                    style = giantsTitle6,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }
    }
}

@Composable
fun TitleSection(isFirstRun: Boolean, nthRun: Int) {
    val title = if (isFirstRun) "첫 운동 찢었다" else "이번 달 ${nthRun}번째 운동"
    val subTitle = if (isFirstRun) "사진을 찍어 추억을 남겨보세요" else "앞으로도 계속 함께 할 거죠?"
    Text(
        title,
        style = giantsTitle1,
        color = WhiteFF,
        fontStyle = FontStyle.Italic
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(subTitle, style = giantsTitle5, color = Color(0xEDEDEDE0))
}

@Composable
fun CameraButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickableSingle { onClick() }
            .background(Primary02, shape = RoundedCornerShape(4.dp))
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        StableImage(
            drawableResId = R.drawable.ic_camera_fill,
            modifier = Modifier
                .width(48.dp)
                .height(38.dp)
        )
    }
}

@Composable
fun CelebrationEffect() {
    LottieImage(
        modifier = Modifier.fillMaxSize(), lottieRes = R.raw.animation_celebration
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun WalkResultContentPreview() {
    WalkResultContent(
        timeText = "12", distanceText = "3.2",
        pathPoints = listOf(
            LatLng(37.5665, 126.9780),
            LatLng(37.5670, 126.9790),
            LatLng(37.5675, 126.9800)
        ),
        isFirstRun = true,
        nthRun = 0,
        onClickCamera = {},
        onBack = {},
    )
}



