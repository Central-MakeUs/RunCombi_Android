package com.combo.runcombi.walk.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun WalkCountdownScreen(
    onCountdownFinished: () -> Unit,
) {
    var count by remember { mutableIntStateOf(3) }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        for (i in 3 downTo 1) {
            count = i
            scale.snapTo(1f)
            scale.animateTo(1.5f, animationSpec = tween(300))
            scale.animateTo(1f, animationSpec = tween(200))
            delay(700)
        }
        onCountdownFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$count",
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(scaleX = scale.value, scaleY = scale.value)
        )
    }
} 