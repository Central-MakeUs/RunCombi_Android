package com.combo.runcombi.walk.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsHeading2
import com.combo.runcombi.feature.walk.R
import kotlinx.coroutines.delay
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import kotlinx.coroutines.launch

@Composable
fun WalkCountdownScreen(
    onCountdownFinished: () -> Unit,
) {
    var count by remember { mutableIntStateOf(3) }
    val scale = remember { Animatable(1f) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.count_down))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 1.0f,
        isPlaying = true,
        restartOnPlay = false
    )

    LaunchedEffect(Unit) {
        for (i in 3 downTo 1) {
            count = i
            scale.snapTo(1f)
            scale.animateTo(1.5f, animationSpec = tween(300))
            scale.animateTo(1f, animationSpec = tween(200))
            delay(500)
        }
        onCountdownFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.size(222.dp),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.matchParentSize()
            )
            Text(
                text = "$count",
                style = giantsHeading2,
                color = Color.White,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(end = 12.dp)
                    .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun PreviewCountdownContent() {
    WalkCountdownScreen(onCountdownFinished = {})
}
