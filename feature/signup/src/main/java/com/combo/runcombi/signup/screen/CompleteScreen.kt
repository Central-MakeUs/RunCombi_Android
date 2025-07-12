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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.LottieImage
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.ext.screenDefaultPadding
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.heading1
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.feature.signup.R
import com.combo.runcombi.signup.viewmodel.SignupViewModel

@Composable
fun CompleteScreen(
    userName: String,
    petName: String,
    onDone: () -> Unit,
) {
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
                onClick = onDone,
                text = "좋아요",
            )
        }
        LottieImage(
            modifier = Modifier.fillMaxSize(), lottieRes = R.raw.animation_celebration
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCompleteScreen() {
    CompleteScreen(userName = "", petName = "", onDone = {})
} 