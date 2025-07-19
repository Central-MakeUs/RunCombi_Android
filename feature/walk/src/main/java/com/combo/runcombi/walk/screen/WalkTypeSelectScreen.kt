package com.combo.runcombi.walk.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title1
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.walk.viewmodel.WalkMainViewModel

@Composable
fun WalkTypeSelectScreen(
    walkMainViewModel: WalkMainViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onTypeSelected: () -> Unit = {},
) {
    val uiState by walkMainViewModel.uiState.collectAsState()
    val selected= uiState.petUiList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }
    val selectedCombis = selected.map { it.pet.name }.joinToString()

    Column(
        modifier = Modifier
            .background(color = Grey01)
    ) {
        RunCombiAppTopBar(
            isVisibleBackBtn = true,
            onBack = onBack,
        )
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Spacer(Modifier.height(56.dp))
            Text(
                text = "오늘은, ${selectedCombis}와 \n" +
                        "어떤 운동을 하실 건가요? ",
                style = title1, color = Color.White,
            )
            Spacer(Modifier.weight(1f))
            RunCombiButton(
                onClick = onTypeSelected,
                textColor = WhiteFF,
                enabledColor = Grey04,
                text = "걷기",
            )
            Spacer(Modifier.height(16.dp))
            RunCombiButton(
                onClick = onTypeSelected,
                textColor = WhiteFF,
                enabledColor = Grey04,
                text = "빠른 걷기",
            )
            Spacer(Modifier.height(16.dp))
            RunCombiButton(
                onClick = onTypeSelected,
                textColor = WhiteFF,
                enabledColor = Grey04,
                text = "슬로우 러닝",
            )
            Spacer(Modifier.height(93.dp))
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun WalkTypeSelectScreenPreview() {
    WalkTypeSelectScreen()
} 