package com.combo.runcombi.history.screen

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
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
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTheme
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.heading2
import com.combo.runcombi.history.model.MemoEvent
import com.combo.runcombi.history.viewmodel.MemoViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MemoScreen(
    runId: Int,
    memo: String,
    onBack: () -> Unit = {},
    viewModel: MemoViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.changeMemo(memo)
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is MemoEvent.MemoSuccess -> {
                    onBack()
                }

                is MemoEvent.Error -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    MemoContent(
        memo = uiState.memo,
        changeMemo = {
            viewModel.changeMemo(it)
        },
        onBack = onBack,
        onComplete = {
            viewModel.storeMemo(runId, it)
        }
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

@Composable
fun MemoContent(
    memo: String = "",
    changeMemo: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onComplete: (String) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
            .padding(horizontal = 20.dp)
    ) {
        Column {
            Spacer(Modifier.height(32.dp))
            Text("메모", color = Color.White, style = heading2)
            Spacer(Modifier.height(4.dp))
            Text("강아지와 함께한 운동은 어떠셨나요?", color = Grey05, style = body1)
            Spacer(Modifier.height(24.dp))
            RunCombiTextField(
                value = memo,
                modifier = Modifier.height(144.dp),
                onValueChange = {
                    changeMemo(it)
                },
                placeholder = "함께한 순간을 짧게 남겨보세요",
                enabled = true,
                singleLine = false,
                maxLength = 100,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text("${memo.length}", color = Grey08, style = body3)
                Text("/100", color = Grey08.copy(alpha = 0.32f), style = body3)
            }
            Spacer(Modifier.weight(1f))
            Row {
                RunCombiButton(
                    text = "이전",
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    enabledColor = Grey04,
                    textColor = Grey08,
                    onClick = {
                        onBack()
                    })
                Spacer(Modifier.width(10.dp))
                RunCombiButton(
                    text = "완료",
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f),
                    onClick = {
                        onComplete(memo)
                    })
            }
            Spacer(Modifier.height(32.dp))
        }

    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun MemoContentPreview() {
    RunCombiTheme {
        MemoContent(
            memo = "강아지와 함께한 산책이 즐거웠어요! 날씨도 좋아서 기분 최고 :)",
            changeMemo = {},
            onBack = {},
            onComplete = {}
        )
    }
}