package com.combo.runcombi.walk.model

import androidx.compose.ui.graphics.Path

data class WalkPath(
    val path: Path,
    val startX: Float,
    val startY: Float,
    val endX: Float,
    val endY: Float
)