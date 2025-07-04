package com.combo.runcombi.core.designsystem.theme

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

object RunCombiTypography {
    private val DefaultTextStyle = TextStyle(
        fontFamily = pretendardFamily,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        )
    )

    // Heading 1
    val heading1 = DefaultTextStyle.copy(
        fontSize = 28.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.SemiBold
    )

    // Title 1
    val title1 = DefaultTextStyle.copy(
        fontSize = 22.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.SemiBold
    )

    // Title 2
    val title2 = DefaultTextStyle.copy(
        fontSize = 20.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.SemiBold
    )

    // Title 3
    val title3 = DefaultTextStyle.copy(
        fontSize = 18.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.SemiBold
    )

    // Title 4
    val title4 = DefaultTextStyle.copy(
        fontSize = 18.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.SemiBold
    )

    // Body 1
    val body1 = DefaultTextStyle.copy(
        fontSize = 16.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Medium
    )

    // Body 2
    val body2 = DefaultTextStyle.copy(
        fontSize = 14.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium
    )

    // Body 3
    val body3 = DefaultTextStyle.copy(
        fontSize = 12.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold
    )
}
