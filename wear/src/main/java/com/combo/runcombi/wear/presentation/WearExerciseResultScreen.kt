package com.combo.runcombi.wear.presentation

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.walk.model.ExerciseType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ExerciseResult(
    val time: String,
    val distance: Double,
    val exerciseType: ExerciseType,
    val member: Member,
    val selectedPets: List<Pet>,
    val isFirstRun: Boolean = false,
    val nthRun: Int = 1,
)

@Composable
fun WearExerciseResultScreen(
    result: ExerciseResult,
    onBackToMain: () -> Unit,
) {
    Scaffold(
        timeText = { TimeText() }
    ) {
        val listState = rememberScalingLazyListState()
        
        ScalingLazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            item {
                // 완료 메시지
                Text(
                    text = "완료!",
                    style = MaterialTheme.typography.title1,
                    color = Primary01,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                // 운동 타입 표시
                Text(
                    text = when (result.exerciseType) {
                        ExerciseType.SLOW_WALKING -> "걷기"
                        ExerciseType.WALKING -> "빠른 걷기"
                        ExerciseType.RUNNING -> "조깅"
                    },
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                )
            }

            item {
                // 운동 통계 - 간단하게
                Text(
                    text = "${result.time} / ${String.format("%.1f", result.distance)}km",
                    style = MaterialTheme.typography.title2,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                )
            }

            item {
                // 함께한 친구들 - 간단하게
                Text(
                    text = "${result.member.nickname} + ${
                        result.selectedPets.map { it.name }.joinToString(", ")
                    }",
                    style = MaterialTheme.typography.body2,
                    color = Primary01,
                    textAlign = TextAlign.Center
                )
            }

            item {
                // 메인으로 돌아가기 버튼
                RunCombiButton(
                    text = "메인",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = onBackToMain,
                    textColor = Color.Black,
                    enabledColor = Primary01
                )
            }
        }
    }
}


@Preview(device = WearDevices.SMALL_ROUND)
@Composable
fun WearExerciseResultScreenPreview() {
    MaterialTheme {
        val sampleMember = Member(
            nickname = "테스터",
            height = 170,
            weight = 70,
            gender = com.combo.runcombi.domain.user.model.Gender.MALE,
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

        val sampleResult = ExerciseResult(
            time = "25:30",
            distance = 3.2,
            exerciseType = ExerciseType.RUNNING,
            member = sampleMember,
            selectedPets = samplePets,
            isFirstRun = false,
            nthRun = 5
        )

        WearExerciseResultScreen(
            result = sampleResult,
            onBackToMain = {}
        )
    }
}
