package com.combo.runcombi.setting.screen

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
import com.combo.runcombi.setting.model.SuggestionEvent
import com.combo.runcombi.setting.viewmodel.SuggestionViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SuggestionScreen(
    onBack: () -> Unit,
    viewModel: SuggestionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SuggestionEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is SuggestionEvent.Success -> {
                    onBack()
                }
            }
        }
    }

    SuggestionContent(
        suggestion = uiState.suggestion,
        changeSuggestion = { suggestion ->
            viewModel.changeSuggestion(suggestion)
        },
        onBack = onBack,
        onComplete = {
            viewModel.suggest()
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
fun SuggestionContent(
    suggestion: String = "",
    changeSuggestion: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onComplete: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
            .padding(horizontal = 20.dp)
    ) {
        Column {
            Spacer(Modifier.height(32.dp))
            Text("런콤비 개선 제안", color = Color.White, style = heading2)
            Spacer(Modifier.height(4.dp))
            Text("작은 의견도 런콤비에겐 큰 힘이 돼요!", color = Grey05, style = body1)
            Spacer(Modifier.height(24.dp))
            RunCombiTextField(
                value = suggestion,
                modifier = Modifier.height(144.dp),
                onValueChange = {
                    changeSuggestion(it)
                },
                placeholder = "의견을 작성해주세요",
                enabled = true,
                singleLine = false,
                maxLength = 100,
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text("${suggestion.length}", color = Grey08, style = body3)
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
                        onComplete()
                    })
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun SuggestionContentPreview() {
    RunCombiTheme {
        SuggestionContent(
            suggestion = "런콤비 앱이 정말 유용해요! 다만 UI를 조금 더 직관적으로 만들면 좋을 것 같습니다.",
            changeSuggestion = {},
            onBack = {},
            onComplete = {}
        )
    }
}