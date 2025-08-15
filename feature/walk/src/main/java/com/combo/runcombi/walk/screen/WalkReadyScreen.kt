package com.combo.runcombi.walk.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsHeading1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle3
import com.combo.runcombi.ui.ext.clickableSingle

@SuppressLint("MissingPermission")
@Composable
fun WalkReadyScreen(
    onBack: () -> Unit,
    onClose: () -> Unit,
    onCompleteReady: () -> Unit,
) {
    Column(modifier = Modifier.background(color = Grey01)) {
        RunCombiAppTopBar(
            isVisibleBackBtn = true,
            isVisibleCloseBtn = true,
            buttonTint = Color.White,
            onBack = onBack,
            onClose = onClose
        )
        Spacer(modifier = Modifier.height(50.dp))
        WalkReadyContent(onCompleteReady = onCompleteReady)
    }
}

@Composable
private fun WalkReadyContent(
    onCompleteReady: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("함께 운동한 시간", style = giantsTitle3, color = Grey08)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "00:00",
                style = giantsHeading1,
                fontStyle = FontStyle.Italic,
                color = Color.White,
                modifier = Modifier.padding(end = 12.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Primary01, shape = RoundedCornerShape(4.dp))
                    .clickableSingle { onCompleteReady() }, contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "시작", style = RunCombiTypography.giantsTitle2, color = Grey01
                )
            }

            Spacer(modifier = Modifier.height(130.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1c1c1c)
@Composable
private fun WalkReadyContentPreview() {
    WalkReadyContent(
        onCompleteReady = {},
    )
} 