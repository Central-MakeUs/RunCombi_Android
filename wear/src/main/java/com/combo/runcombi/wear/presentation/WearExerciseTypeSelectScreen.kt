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
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.walk.model.ExerciseType

@Composable
fun WearExerciseTypeSelectScreen(
    selectedPets: List<Pet>,
    onExerciseTypeSelected: (ExerciseType) -> Unit,
    onBack: () -> Unit
) {
    val selectedPetNames = selectedPets.joinToString { it.name }
    
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
                Text(
                    text = "운동 선택",
                    style = MaterialTheme.typography.title2,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                )
            }
            
            item {
                Text(
                    text = "${selectedPetNames}와 함께",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                    textAlign = TextAlign.Center
                )
            }
            
            item {
                // 운동 타입 버튼들
                ExerciseTypeButton(
                    text = "걷기",
                    onClick = { onExerciseTypeSelected(ExerciseType.SLOW_WALKING) }
                )
            }
            
            item {
                ExerciseTypeButton(
                    text = "빠른 걷기",
                    onClick = { onExerciseTypeSelected(ExerciseType.WALKING) }
                )
            }
            
            item {
                ExerciseTypeButton(
                    text = "조깅",
                    onClick = { onExerciseTypeSelected(ExerciseType.RUNNING) }
                )
            }
            
            item {
                // 뒤로가기 버튼
                RunCombiButton(
                    text = "뒤로",
                    modifier = Modifier.padding(horizontal = 16.dp),
                    onClick = onBack
                )
            }
        }
    }
}

@Composable
private fun ExerciseTypeButton(
    text: String,
    onClick: () -> Unit
) {
    RunCombiButton(
        text = text,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 16.dp),
        textColor = Color.Black,
        enabledColor = Primary01
    )
}

@Preview(device = WearDevices.SMALL_ROUND)
@Composable
fun WearExerciseTypeSelectScreenPreview() {
    MaterialTheme {
        val samplePets = listOf(
            Pet(
                id = 1,
                name = "멍멍이",
                age = 3,
                weight = 25.0,
                runStyle = com.combo.runcombi.domain.user.model.RunStyle.RUNNING,
                profileImageUrl = ""
            ),
            Pet(
                id = 2,
                name = "야옹이",
                age = 2,
                weight = 4.5,
                runStyle = com.combo.runcombi.domain.user.model.RunStyle.WALKING,
                profileImageUrl = ""
            )
        )

        WearExerciseTypeSelectScreen(
            selectedPets = samplePets,
            onExerciseTypeSelected = {},
            onBack = {}
        )
    }
}