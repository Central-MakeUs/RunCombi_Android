package com.combo.runcombi.signup.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.combo.runcombi.core.designsystem.component.LottieImage
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.ui.ext.screenDefaultPadding
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.heading1
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.feature.signup.R
import com.combo.runcombi.core.designsystem.component.RunCombiBottomSheet

@Composable
fun CompleteScreen(
    userName: String,
    petName: String,
    onDone: (Boolean) -> Unit,
) {
    val showSheet = remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .screenDefaultPadding()
        ) {
            Spacer(Modifier.height(95.dp))
            Text(
                text = "$userName 님, 가입을 축하합니다!",
                textAlign = TextAlign.Center,
                style = heading1,
                color = WhiteFF,
                modifier = Modifier.padding(horizontal = 80.dp)
            )
            Spacer(Modifier.height(157.dp))
            StableImage(
                drawableResId = R.drawable.complete_dog,
                modifier = Modifier
                    .height(90.dp)
                    .width(151.02.dp)
            )
            Spacer(Modifier.height(157.dp))
            Text(
                text = "이제 ${petName}와 함께 건강한 일상을 채워나가 볼까요?",
                textAlign = TextAlign.Center,
                style = body1,
                color = WhiteFF,
                modifier = Modifier.padding(horizontal = 80.dp)
            )
            Spacer(Modifier.weight(1f))
            RunCombiButton(
                onClick = { showSheet.value = true },
                text = "좋아요",
            )
        }
        LottieImage(
            modifier = Modifier.fillMaxSize(), lottieRes = R.raw.animation_celebration
        )

        RunCombiBottomSheet(
            show = showSheet.value,
            onDismiss = { showSheet.value = false },
            onAccept = {
                showSheet.value = false
                onDone(true)
            },
            onCancel = {
                showSheet.value = false
                onDone(false)
            },
            title = "혹시 콤비가 더 있나요?",
            subtitle = "런콤비는 최대 2마리의 반려견과 함께할 수 있어요. 지금 바로 반려견을 추가해볼까요?",
            acceptButtonText = "추가",
            cancelButtonText = "괜찮아요"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCompleteScreen() {
    CompleteScreen(userName = "", petName = "", onDone = { _ -> })
} 