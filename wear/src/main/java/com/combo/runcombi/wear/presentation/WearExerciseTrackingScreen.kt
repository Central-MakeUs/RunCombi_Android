package com.combo.runcombi.wear.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.component.NetworkImage
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.walk.model.ExerciseType
import com.combo.runcombi.wear.R
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ExerciseTrackingState(
    val isTracking: Boolean = false,
    val isPaused: Boolean = false,
    val startTime: Long = 0L,
    val pausedTime: Long = 0L,
    val totalPausedDuration: Long = 0L,
    val distance: Double = 0.0,
    val currentTime: String = "00:00:00",
)

@Composable
fun WearExerciseTrackingScreen(
    member: Member,
    selectedPets: List<Pet>,
    exerciseType: ExerciseType,
    onFinish: (String, Double) -> Unit,
    onBack: () -> Unit,
    viewModel: WearExerciseViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var isInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(selectedPets, exerciseType) {
        if (!isInitialized && selectedPets.isNotEmpty()) {
            viewModel.startExercise(selectedPets, exerciseType)
            isInitialized = true
        }
    }

    LaunchedEffect(uiState.isTracking, uiState.isPaused) {
        while (uiState.isTracking) {
            delay(1000)
            
            if (!uiState.isPaused) {
                val currentTime = System.currentTimeMillis()
                val elapsed = currentTime - uiState.startTime - uiState.totalPausedDuration
                val timeString = formatTime(elapsed)
                viewModel.updateTime(timeString)
            }
        }
    }

    val listState = rememberScalingLazyListState()
    
    ScalingLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp)
    ) {
        item {
            Text(
                text = "운동 중",
                style = MaterialTheme.typography.body1,
                color = Grey08,
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = uiState.currentTime,
                style = MaterialTheme.typography.title1,
                color = Primary01,
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = "${String.format("%.1f", uiState.distance)}km",
                style = MaterialTheme.typography.body2,
                color = Grey08,
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = "${member.nickname} + ${selectedPets.map { it.name }.joinToString(", ")}",
                style = MaterialTheme.typography.body2,
                color = Primary01,
                textAlign = TextAlign.Center
            )
        }

        item {
            // 로딩 표시
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                // 컨트롤 버튼들 
                if (!uiState.isTracking) {
                    // 시작 버튼
                    RunCombiButton(
                        text = "시작",
                        onClick = {
                            viewModel.startExercise(selectedPets, exerciseType)
                        }
                    )
                } else {
                    // 운동 중 - 정지/완료 버튼
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 운동 상태에 따른 버튼 표시
                        when {
                            uiState.isTracking && !uiState.isPaused -> {
                                // 운동 중 - 일시정지 버튼
                                RunCombiButton(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    text = "일시정지",
                                    onClick = { viewModel.pauseExercise() }
                                )
                            }
                            uiState.isTracking && uiState.isPaused -> {
                                // 일시정지 상태 - 재개 버튼
                                RunCombiButton(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    text = "재개",
                                    onClick = { viewModel.resumeExercise() }
                                )
                            }
                        }
                        
                        // 완료 버튼 (항상 표시, 정지 상태에서만 활성화)
                        RunCombiButton(
                            text = "완료",
                            modifier = Modifier.padding(horizontal = 16.dp),
                            onClick = { 
                                if (uiState.isPaused) {
                                    viewModel.finishExercise()
                                    onFinish(uiState.currentTime, uiState.distance)
                                } else {
                                    // 운동 중이면 먼저 정지
                                    viewModel.pauseExercise()
                                }
                            },
                            textColor = if (uiState.isPaused) Color.White else Grey08,
                            enabledColor = if (uiState.isPaused) MaterialTheme.colors.error else Grey02,
                            enabled = true
                        )
                    }
                }
            }
        }

        item {
            uiState.error?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.body2,
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }

        if (uiState.error != null) {
            item {
                RunCombiButton(
                    text = "뒤로",
                    onClick = onBack,
                    modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp)
                )
            }
        }
    }
}

@Composable
private fun CombiInfo(
    member: Member,
    pets: List<Pet>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "함께하는 콤비",
            style = MaterialTheme.typography.body2,
            color = Grey08
        )

        // 유저 정보
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(Primary01, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.nickname.take(1),
                    color = Color.White,
                    style = MaterialTheme.typography.body2
                )
            }
            Text(
                text = member.nickname,
                style = MaterialTheme.typography.body2,
                color = Grey08
            )
        }

        // 펫 정보
        pets.forEach { pet ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NetworkImage(
                    imageUrl = pet.profileImageUrl,
                    drawableResId = android.R.drawable.ic_menu_gallery,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                )
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.body2,
                    color = Grey08
                )
            }
        }
    }
}


private fun formatTime(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}


@Preview(device = WearDevices.SMALL_ROUND)
@Composable
fun WearExerciseTrackingScreenPreview() {
    val sampleMember = Member(
        nickname = "테스트유저",
        gender = com.combo.runcombi.domain.user.model.Gender.MALE,
        height = 175,
        weight = 70,
        profileImageUrl = ""
    )

    val samplePets = listOf(
        Pet(
            id = 1,
            name = "멍멍이",
            age = 3,
            weight = 25.0,
            runStyle = com.combo.runcombi.domain.user.model.RunStyle.RUNNING,
            profileImageUrl = ""
        )
    )

    WearExerciseTrackingScreen(
        member = sampleMember,
        selectedPets = samplePets,
        exerciseType = ExerciseType.RUNNING,
        onFinish = { _, _ -> },
        onBack = {}
    )
}
