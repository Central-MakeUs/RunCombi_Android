@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class, ExperimentalFoundationApi::class
)

package com.combo.runcombi.walk.screen

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Looper
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.analytics.logScreenView
import com.combo.runcombi.analytics.logWalkCompleted
import com.combo.runcombi.core.designsystem.component.NetworkImage
import com.combo.runcombi.core.designsystem.component.RunCombiBottomSheet
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey07
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Grey09
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle4
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle6
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.feature.walk.R
import com.combo.runcombi.ui.util.FormatUtils
import com.combo.runcombi.walk.model.BottomSheetType
import com.combo.runcombi.walk.model.WalkMemberUiModel
import com.combo.runcombi.walk.model.WalkPetUIModel
import com.combo.runcombi.walk.model.WalkTrackingEvent
import com.combo.runcombi.walk.model.WalkUiState
import com.combo.runcombi.walk.model.getBottomSheetContent
import com.combo.runcombi.walk.viewmodel.WalkMainViewModel
import com.combo.runcombi.walk.viewmodel.WalkTrackingViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("MissingPermission")
@Composable
fun WalkTrackingScreen(
    onFinish: () -> Unit,
    onBack: () -> Unit,
    walkMainViewModel: WalkMainViewModel = hiltViewModel(),
    walkRecordViewModel: WalkTrackingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val analyticsHelper = walkRecordViewModel.analyticsHelper
    val uiState by walkRecordViewModel.uiState.collectAsStateWithLifecycle()
    val isPaused = uiState.isPaused
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val isInitialized = rememberSaveable { mutableStateOf(false) }

    val showSheet = remember { mutableStateOf(BottomSheetType.NONE) }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    walkRecordViewModel.addPathPointFromService(
                        location.latitude, location.longitude, location.accuracy, location.time
                    )
                }
            }
        }
    }

    LaunchedEffect(isInitialized.value) {
        if (!isInitialized.value) {
            analyticsHelper.logScreenView("WalkTrackingScreen")

            walkMainViewModel.startRun()

            val member = walkMainViewModel.walkData.value.member
            val exerciseType = walkMainViewModel.walkData.value.exerciseType
            val selectedPetList = walkMainViewModel.walkData.value.petList
            if (member != null) {
                walkRecordViewModel.initWalkData(exerciseType, member, selectedPetList)
            }
            isInitialized.value = true
        }

        walkRecordViewModel.eventFlow.collectLatest { event ->
            if (event is WalkTrackingEvent.ShowBottomSheet) {
                showSheet.value = event.type
            }
        }
    }

    DisposableEffect(isPaused) {
        if (!isPaused) {
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 2000)
                .setMinUpdateIntervalMillis(1000).build()

            fusedLocationClient.requestLocationUpdates(
                request, locationCallback, Looper.getMainLooper()
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
        onFinishClick = {
            walkRecordViewModel.emitShowBottomSheet(BottomSheetType.FINISH)
        }
    )

    val content = getBottomSheetContent(showSheet.value)
    content?.let {
        RunCombiBottomSheet(
            show = true,
            onDismiss = { showSheet.value = BottomSheetType.NONE },
            onAccept = {
                when (showSheet.value) {
                    BottomSheetType.FINISH -> {
                        // 산책 완료 이벤트 로깅
                        val duration = FormatUtils.formatTime(uiState.time)
                        val distance = String.format("%.2f", uiState.distance / 1000.0)
                        analyticsHelper.logWalkCompleted(duration, "${distance}km")

                        walkMainViewModel.setResultData(
                            time = uiState.time,
                            distance = uiState.distance,
                            pathPoints = uiState.pathPoints,
                            member = uiState.walkMemberUiModel,
                            petList = uiState.walkPetUIModelList ?: emptyList()
                        )
                        onFinish()
                    }

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
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Grey01)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("함께 운동한 시간", color = Grey08, style = RunCombiTypography.giantsTitle3)
                Spacer(Modifier.height(10.45.dp))
                TimeDisplayLarge(uiState.time)
                Spacer(Modifier.height(26.55.dp))
                DistanceDisplayLarge(uiState.distance)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (uiState.isPaused) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FinishButtonLongPress(onLongClick = onFinishClick)
                        ResumeButton(onClick = onPauseToggle)
                    }
                } else {
                    PauseButton(onClick = onPauseToggle)
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Grey01),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(72.dp))
            Text("함께 운동한 시간", color = Grey08, style = RunCombiTypography.giantsTitle3)
            Spacer(Modifier.height(10.45.dp))
            TimeDisplayLarge(uiState.time)
            Spacer(Modifier.height(26.55.dp))
            DistanceDisplayLarge(uiState.distance)
            Spacer(Modifier.weight(1f))
            if (uiState.walkMemberUiModel != null && uiState.walkPetUIModelList != null) CombiCalorieList(
                member = uiState.walkMemberUiModel, petUiList = uiState.walkPetUIModelList
            )

            Spacer(Modifier.weight(1f))
            if (uiState.isPaused) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FinishButtonLongPress(onLongClick = onFinishClick)
                    ResumeButton(onClick = onPauseToggle)
                }
            } else {
                PauseButton(onClick = onPauseToggle)
            }
            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun ResumeButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(color = Primary01, shape = RoundedCornerShape(4.dp))
            .size(100.dp),
        contentAlignment = Alignment.Center) {
        StableImage(
            drawableResId = R.drawable.ic_resume, modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        )
    }
}

@Composable
fun FinishButtonLongPress(onLongClick: () -> Unit) {
    Box(
        modifier = Modifier
            .combinedClickable(
                onClick = {}, onLongClick = onLongClick
            )
            .background(color = Grey09, shape = RoundedCornerShape(4.dp))
            .size(100.dp),
        contentAlignment = Alignment.Center
    ) {
        StableImage(
            drawableResId = R.drawable.ic_stop, modifier = Modifier
                .width(48.dp)
                .height(48.dp)
        )
    }
}

@Composable
fun TimeDisplayLarge(time: Int) {
    val formattedTime = FormatUtils.formatTime(time)
    Text(
        text = formattedTime,
        color = Color.White,
        fontStyle = FontStyle.Italic,
        style = RunCombiTypography.giantsHeading1,
        modifier = Modifier.padding(end = 12.dp)
    )
}

@Composable
fun DistanceDisplayLarge(distance: Double) {
    val formattedDistance = FormatUtils.formatDistance(distance / 1000.0)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = formattedDistance,
            color = Grey06,
            fontStyle = FontStyle.Italic,
            style = giantsTitle2,
            modifier = Modifier
                .padding(end = 8.dp)
                .alignByBaseline()
        )
        Text(
            text = "Km", color = Grey06, style = giantsTitle6, modifier = Modifier.alignByBaseline()
        )
    }
}

@Composable
private fun CombiCalorieList(
    member: WalkMemberUiModel,
    petUiList: List<WalkPetUIModel>,
) {
    val size = petUiList.size

    Row(
        modifier = Modifier
            .height(155.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        MemberCalorieContent(
            modifier = Modifier
                .fillMaxHeight(),
            size = size,
            member = member
        )
        petUiList.forEachIndexed { index, pet ->
            PetCalorieContent(
                modifier = Modifier
                    .fillMaxHeight(),
                pet = pet,
                size = size,
                isCenter = petUiList.size > 1 && index == 0,
            )
        }
    }
}

private fun getMemberWidth(size: Int): Dp =
    if (size > 1) 99.7826.dp else 152.dp

private fun getPetWidth(size: Int, isCenter: Boolean): Dp =
    when {
        size == 1 -> 152.dp
        size == 2 && isCenter -> 106.4348.dp
        else -> 99.7826.dp
    }

@Composable
private fun MemberCalorieContent(
    modifier: Modifier = Modifier,
    member: WalkMemberUiModel,
    size: Int,
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(getMemberWidth(size)),
        contentAlignment = Alignment.Center
    ) {
        StableImage(
            drawableResId = if (size == 1) R.drawable.ic_walk_box_left else R.drawable.ic_walk_box_type_b_left,
            modifier = Modifier.fillMaxSize()
        )
        CalorieContent(
            modifier = Modifier.padding(end = 12.dp),
            name = member.member.nickname,
            imageUrl = member.member.profileImageUrl ?: "",
            drawableResId = R.drawable.person_profile,
            calorie = member.calorie,
            size = size,
        )
    }
}

@Composable
private fun PetCalorieContent(
    modifier: Modifier = Modifier,
    pet: WalkPetUIModel,
    size: Int,
    isCenter: Boolean = false,
) {
    val boxImage =
        if (isCenter) R.drawable.ic_walk_box_type_b_center else (if (size == 1) R.drawable.ic_walk_box_right else R.drawable.ic_walk_box_type_b_right)
    val startPadding = 12.dp
    val endPadding = if (isCenter) 12.dp else 0.dp

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(getPetWidth(size, isCenter)),
        contentAlignment = Alignment.Center
    ) {
        StableImage(
            drawableResId = boxImage, modifier = Modifier.fillMaxSize()
        )
        CalorieContent(
            modifier = Modifier.padding(start = startPadding, end = endPadding),
            name = pet.pet.name,
            imageUrl = pet.pet.profileImageUrl ?: "",
            drawableResId = R.drawable.ic_pet_defalut,
            calorie = pet.calorie,
            size = size,
        )
    }
}

@Composable
fun CalorieContent(
    modifier: Modifier = Modifier,
    name: String, imageUrl: String, drawableResId: Int, calorie: Int, size: Int,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NetworkImage(
            contentScale = ContentScale.Crop,
            imageUrl = imageUrl,
            drawableResId = drawableResId,
            modifier = Modifier
                .background(color = Primary01)
                .size(42.dp)
                .padding(2.dp)
                .clip(RoundedCornerShape(4.dp)),
        )

        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = name,
            color = Grey07,
            style = body2,
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = calorie.toString(),
                color = Grey07,
                style = if (size == 1) giantsTitle2 else giantsTitle4,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = " Kcal",
                color = Grey07,
                style = giantsTitle6,
                modifier = Modifier.alignByBaseline()
            )
        }
    }
}

@Composable
fun PauseButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(color = Grey02, shape = RoundedCornerShape(4.dp))
            .size(100.dp),
        contentAlignment = Alignment.Center) {
        StableImage(
            drawableResId = R.drawable.ic_pause, modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun WalkTrackingContentPreview() {
    val member = Member(
        nickname = "홍길동", gender = Gender.MALE, height = 175, weight = 70, profileImageUrl = null
    )
    val petList = listOf(
        WalkPetUIModel(
            pet = Pet(
                id = 0,
                name = "멍멍이",
                age = 3,
                weight = 8.5,
                runStyle = RunStyle.WALKING,
                profileImageUrl = null
            ), calorie = 56
        ), WalkPetUIModel(
            pet = Pet(
                id = 1,
                name = "야옹이",
                age = 2,
                weight = 4.2,
                runStyle = RunStyle.SLOW_WALKING,
                profileImageUrl = null
            ), calorie = 78
        )
    )

    WalkTrackingContent(
        uiState = WalkUiState(
            time = 1234, distance = 2.5, isPaused = false, walkMemberUiModel = WalkMemberUiModel(
                member = member, calorie = 123
            ), walkPetUIModelList = petList
        ), onPauseToggle = {}, onCancelClick = {}, onFinishClick = {})
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun CombiCalorieListPreview() {
    val member = WalkMemberUiModel(
        member = Member(
            nickname = "홍길동",
            gender = Gender.MALE,
            height = 175,
            weight = 70,
            profileImageUrl = null
        ), calorie = 123
    )
    val petList = listOf(
        WalkPetUIModel(
            pet = Pet(
                id = 1,
                name = "야옹이",
                age = 2,
                weight = 4.2,
                runStyle = RunStyle.SLOW_WALKING,
                profileImageUrl = null
            ), calorie = 78
        )
    )
    CombiCalorieList(member = member, petUiList = petList)
}

