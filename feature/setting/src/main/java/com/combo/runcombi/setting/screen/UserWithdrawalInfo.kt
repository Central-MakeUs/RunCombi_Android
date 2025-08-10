package com.combo.runcombi.setting.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey03
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.Primary02
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle6
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.heading2
import com.combo.runcombi.setting.model.UserWithdrawalInfoEvent
import com.combo.runcombi.setting.viewmodel.UserWithdrawalInfoViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserWithdrawalInfoScreen(
    onBack: () -> Unit,
    onClickNext: () -> Unit,
    viewModel: UserWithdrawalInfoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UserWithdrawalInfoEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            RunCombiAppTopBar(
                onBack = onBack,
                title = "",
                padding = PaddingValues(8.dp)
            )

            UserWithdrawalInfoContent(
                onNextClick = onClickNext,
                petList = uiState.petList,
                imageCount = uiState.imageCount,
                runCount = uiState.runCount
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0x80000000)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary01)
                }
            }

        }
    }
}

@Composable
fun UserWithdrawalInfoContent(
    onNextClick: () -> Unit,
    imageCount: Int,
    petList: List<String>,
    runCount: Int,
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "정말 런콤비를\n떠나시는 건가요?",
            style = heading2,
            color = Grey08,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "콤비와 함께한 추억들이 모두 사라져요.",
            style = body2,
            color = Grey08.copy(alpha = 0.72f),
        )

        Spacer(modifier = Modifier.height(40.dp))

        DeletedDataSection(
            imageCount = imageCount,
            petList = petList,
            runCount = runCount
        )

        Spacer(modifier = Modifier.height(40.dp))

        WarningSection()

        Spacer(modifier = Modifier.height(40.dp))

        RunCombiButton(
            text = "다음",
            onClick = onNextClick,
            enabledColor = Primary01,
            textColor = Grey03
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun DeletedDataSection(
    imageCount: Int,
    petList: List<String>,
    runCount: Int,
) {
    Column {
        Text(
            text = "삭제될 데이터",
            style = body1,
            color = Grey08,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        DeletedDataItem(
            title = "등록된 콤비",
            value = petList.joinToString(separator = ", ")
        )

        Spacer(modifier = Modifier.height(16.dp))

        DeletedDataItem(
            title = "저장된 운동 기록",
            value = "${runCount}개"
        )

        Spacer(modifier = Modifier.height(16.dp))

        DeletedDataItem(
            title = "저장된 사진",
            value = "${imageCount}장"
        )
    }
}

@Composable
private fun DeletedDataItem(
    title: String,
    value: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Grey02.copy(alpha = 0.5f),
                RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = body2,
            color = Grey08.copy(alpha = 0.78f),
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = giantsTitle6,
            color = Primary02.copy(alpha = 0.78f)
        )
    }
}

@Composable
private fun WarningSection() {
    Column {
        Text(
            text = "주의사항",
            style = body3,
            color = Grey08.copy(alpha = 0.56f),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "• 삭제된 데이터는 재가입시에도 복구되지 않으니,\n  신중하게 고민해주세요.",
            style = body3,
            color = Grey08.copy(alpha = 0.56f),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "• 동일한 계정으로는 24시간 후 재가입이 가능해요.",
            style = body3,
            color = Grey08.copy(alpha = 0.56f),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserWithdrawalInfoScreen() {
    UserWithdrawalInfoScreen(
        onBack = {},
        onClickNext = {}
    )
}