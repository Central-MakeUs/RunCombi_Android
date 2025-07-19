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
    polygonSize: Dp = 32.dp,
    topLeftAngle: Double = 60.0,
    topRightAngle: Double = 60.0,
    bottomRightAngle: Double = 60.0,
    bottomLeftAngle: Double = 60.0
) = clip(
    CustomPolygonShape(
        topLeft, topRight, bottomLeft, bottomRight, polygonSize,
        topLeftAngle, topRightAngle, bottomRightAngle, bottomLeftAngle
    )
)

@Stable
class CustomPolygonShape(
    private val topLeft: Boolean,
    private val topRight: Boolean,
    private val bottomLeft: Boolean,
    private val bottomRight: Boolean,
    private val polygonSize: Dp,
    private val topLeftAngle: Double = 60.0,
    private val topRightAngle: Double = 60.0,
    private val bottomRightAngle: Double = 60.0,
    private val bottomLeftAngle: Double = 60.0
) : Shape {
    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val px = with(density) { polygonSize.toPx() }
        val width = size.width
        val height = size.height

        val topLeftRad = Math.toRadians(topLeftAngle)
        val topRightRad = Math.toRadians(topRightAngle)
        val bottomRightRad = Math.toRadians(bottomRightAngle)
        val bottomLeftRad = Math.toRadians(bottomLeftAngle)

        val topLeftX = px * cos(topLeftRad).toFloat()
        val topLeftY = px * sin(topLeftRad).toFloat()
        val topRightX = px * cos(topRightRad).toFloat()
        val topRightY = px * sin(topRightRad).toFloat()
        val bottomRightX = px * cos(bottomRightRad).toFloat()
        val bottomRightY = px * sin(bottomRightRad).toFloat()
        val bottomLeftX = px * cos(bottomLeftRad).toFloat()
        val bottomLeftY = px * sin(bottomLeftRad).toFloat()

        val path = Path()

        if (topLeft) path.moveTo(topLeftX, 0f) else path.moveTo(0f, 0f)
        if (topRight) path.lineTo(width - topRightX, 0f) else path.lineTo(width, 0f)
        if (topRight) path.lineTo(width, topRightY) else path.lineTo(width, 0f)
        if (bottomRight) path.lineTo(width, height - bottomRightY) else path.lineTo(width, height)
        if (bottomRight) path.lineTo(width - bottomRightX, height) else path.lineTo(width, height)
        if (bottomLeft) path.lineTo(bottomLeftX, height) else path.lineTo(0f, height)
        if (bottomLeft) path.lineTo(0f, height - bottomLeftY) else path.lineTo(0f, height)
        if (topLeft) path.lineTo(0f, topLeftY) else path.lineTo(0f, 0f)

        path.close()
        return Outline.Generic(path)
    }
}
