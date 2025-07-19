package com.combo.runcombi.ui.ext

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.combo.runcombi.ui.util.MultipleEventsCutter
import com.combo.runcombi.ui.util.get
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Outline
import kotlin.math.sqrt
import kotlin.math.cos
import kotlin.math.sin

fun Modifier.clickableWithoutRipple(
    onClick: () -> Unit,
): Modifier {
    return then(
        Modifier.clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            onClick = onClick
        )
    )
}

fun Modifier.clickableSingle(
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit,
) = composed {
    val multipleEventsCutter = remember { MultipleEventsCutter.get() }
    Modifier.clickable(
        enabled = enabled,
        onClickLabel = onClickLabel,
        onClick = { multipleEventsCutter.processEvent { onClick() } },
        role = role,
        indication = LocalIndication.current,
        interactionSource = remember { MutableInteractionSource() }
    )
}

fun Modifier.screenDefaultPadding(): Modifier {
    return this.then(
        Modifier.padding(start = 20.dp, end = 20.dp, bottom = 39.dp)
    )
}

fun Modifier.customPolygonClip(
    topLeft: Boolean = false,
    topRight: Boolean = false,
    bottomLeft: Boolean = false,
    bottomRight: Boolean = false,
    polygonSize: Dp = 32.dp
) = clip(
    CustomPolygonShape(
        topLeft, topRight, bottomLeft, bottomRight, polygonSize
    )
)

@Stable
class CustomPolygonShape(
    private val topLeft: Boolean,
    private val topRight: Boolean,
    private val bottomLeft: Boolean,
    private val bottomRight: Boolean,
    private val polygonSize: Dp
) : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val px = with(density) { polygonSize.toPx() }
        val angle = Math.toRadians(60.0)
        val offsetX = px * cos(angle).toFloat()
        val offsetY = px * sin(angle).toFloat()
        val width = size.width
        val height = size.height

        val path = Path()

        // Top Left
        if (topLeft) path.moveTo(offsetX, 0f) else path.moveTo(0f, 0f)
        // Top Right
        if (topRight) path.lineTo(width - offsetX, 0f) else path.lineTo(width, 0f)
        if (topRight) path.lineTo(width, offsetY) else path.lineTo(width, 0f)
        // Bottom Right
        if (bottomRight) path.lineTo(width, height - offsetY) else path.lineTo(width, height)
        if (bottomRight) path.lineTo(width - offsetX, height) else path.lineTo(width, height)
        // Bottom Left
        if (bottomLeft) path.lineTo(offsetX, height) else path.lineTo(0f, height)
        if (bottomLeft) path.lineTo(0f, height - offsetY) else path.lineTo(0f, height)
        if (topLeft) path.lineTo(0f, offsetY) else path.lineTo(0f, 0f)

        path.close()
        return Outline.Generic(path)
    }
}
