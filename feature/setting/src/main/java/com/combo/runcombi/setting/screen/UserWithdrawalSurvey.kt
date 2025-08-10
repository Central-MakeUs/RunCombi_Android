package com.combo.runcombi.setting.screen

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar
import com.combo.runcombi.core.designsystem.component.RunCombiCheckbox
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.Primary02
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.heading2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title3
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.setting.model.UserWithdrawalSurveyEvent
import com.combo.runcombi.setting.model.UserWithdrawalSurveyUiState
import com.combo.runcombi.setting.model.WithdrawalReason
import com.combo.runcombi.setting.viewmodel.UserWithdrawalSurveyViewModel
import com.combo.runcombi.ui.ext.clickableSingle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserWithdrawalSurveyScreen(
    onBack: () -> Unit,
    onComplete: () -> Unit,
    viewModel: UserWithdrawalSurveyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UserWithdrawalSurveyEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is UserWithdrawalSurveyEvent.WithdrawSuccess -> {
                    onComplete()
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

            UserWithdrawalSurveyContent(
                uiState = uiState,
                onReasonSelected = { viewModel.updateWithdrawalReason(it) },
                onAdditionalReasonChanged = { viewModel.updateAdditionalReason(it) },
                onWithdrawalClick = { viewModel.tryWithDraw() }
            )
        }

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

@Composable
fun UserWithdrawalSurveyContent(
    uiState: UserWithdrawalSurveyUiState,
    onReasonSelected: (WithdrawalReason) -> Unit,
    onAdditionalReasonChanged: (String) -> Unit,
    onWithdrawalClick: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "떠나시는 이유를\n알려주세요",
            style = heading2,
            color = Grey08,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "다시 사용하고 싶도록 개선해볼게요!",
            style = body3,
            color = Grey08.copy(alpha = 0.72f),
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

        WithdrawalReasonSection(
            selectedReasonList = uiState.selectedReason,
            onReasonSelected = onReasonSelected
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 기타를 선택한 경우에만 추가 사유 입력 표시
        if (uiState.selectedReason.contains(WithdrawalReason.OTHER)) {
            AdditionalReasonSection(
                text = uiState.additionalReason,
                onTextChanged = onAdditionalReasonChanged,
            )
            Spacer(modifier = Modifier.height(40.dp))
        }

        WithdrawalButton(
            onClick = onWithdrawalClick,
            isEnabled = uiState.selectedReason.isNotEmpty() &&
                    (uiState.selectedReason.contains(WithdrawalReason.OTHER) && uiState.additionalReason.trim()
                        .isNotEmpty() ||
                            !uiState.selectedReason.contains(WithdrawalReason.OTHER))
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun WithdrawalReasonSection(
    selectedReasonList: List<WithdrawalReason>,
    onReasonSelected: (WithdrawalReason) -> Unit,
) {
    Column {
        Text(
            text = "탈퇴 이유",
            style = title3,
            color = Grey08,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val reasons = listOf(
            WithdrawalReason.RECORD_CLEANUP to "기록을 정리하고 싶어요",
            WithdrawalReason.DIFFICULT_USAGE to "사용 방법이 어려워요",
            WithdrawalReason.INSUFFICIENT_FEATURES to "기능이 부족하거나 불편했어요",
            WithdrawalReason.RARELY_USED to "자주 쓰지 않게 되었어요",
            WithdrawalReason.OTHER to "기타"
        )

        reasons.forEach { (reason, text) ->
            ReasonItem(
                text = text,
                isSelected = selectedReasonList.contains(reason),
                onClick = { onReasonSelected(reason) }
            )
            if (reason != WithdrawalReason.OTHER) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ReasonItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = body1,
            color = Grey08,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        RunCombiCheckbox(
            checked = isSelected,
            onCheckedChange = { onClick() },
            checkedColor = Primary02,
            uncheckedColor = Grey04
        )


    }
}

@Composable
private fun AdditionalReasonSection(
    text: String,
    onTextChanged: (String) -> Unit,
) {
    Column {
        RunCombiTextField(
            value = text,
            modifier = Modifier.height(144.dp),
            onValueChange = {
                onTextChanged(it)
            },
            placeholder = "사유를 입력해주세요",
            enabled = true,
            singleLine = false,
            maxLength = 100,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text("${text.length}", color = Grey08, style = body3)
            Text("/100", color = Grey08.copy(alpha = 0.32f), style = body3)
        }


    }
}

@Composable
private fun WithdrawalButton(
    onClick: () -> Unit,
    isEnabled: Boolean = true,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = if (isEnabled) Color(0xFFFC5555) else Grey04,
                shape = RoundedCornerShape(8.dp)
            )
            .clickableSingle(enabled = isEnabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "회원 탈퇴",
            style = title3,
            color = if (isEnabled) WhiteFF else Grey05,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUserWithdrawalSurveyScreen() {
    UserWithdrawalSurveyScreen(
        onBack = {},
        onComplete = {}
    )
}
