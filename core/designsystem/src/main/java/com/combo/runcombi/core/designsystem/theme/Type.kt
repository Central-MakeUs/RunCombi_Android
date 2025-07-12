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

    private val GiantsTextStyle = TextStyle(
        fontFamily = giantsFamily,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Center,
            trim = LineHeightStyle.Trim.None
        )
    )

    val heading1 = DefaultTextStyle.copy(
        fontSize = 28.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.SemiBold
    )

    val heading2 = DefaultTextStyle.copy(
        fontSize = 24.sp,
        lineHeight = 36.sp,
        fontWeight = FontWeight.SemiBold
    )

    val title1 = DefaultTextStyle.copy(
        fontSize = 22.sp,
        lineHeight = 34.sp,
        fontWeight = FontWeight.SemiBold
    )

    val title2 = DefaultTextStyle.copy(
        fontSize = 20.sp,
        lineHeight = 32.sp,
        fontWeight = FontWeight.SemiBold
    )

    val title3 = DefaultTextStyle.copy(
        fontSize = 18.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.SemiBold
    )

    val title4 = DefaultTextStyle.copy(
        fontSize = 18.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.SemiBold
    )

    val body1 = DefaultTextStyle.copy(
        fontSize = 16.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Medium
    )

    val body2 = DefaultTextStyle.copy(
        fontSize = 14.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Medium
    )

    val body3 = DefaultTextStyle.copy(
        fontSize = 12.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.SemiBold
    )

    val giantsHeading1 = GiantsTextStyle.copy(
        fontSize = 70.sp,
        lineHeight = 78.sp,
        fontWeight = FontWeight.Normal
    )

    val giantsHeading2 = GiantsTextStyle.copy(
        fontSize = 96.sp,
        lineHeight = 110.sp,
        fontWeight = FontWeight.Bold
    )

    val giantsTitle1 = GiantsTextStyle.copy(
        fontSize = 32.sp,
        lineHeight = 30.sp,
        fontWeight = FontWeight.Normal
    )
    val giantsTitle2 = GiantsTextStyle.copy(
        fontSize = 24.sp,
        lineHeight = 28.sp,
        fontWeight = FontWeight.Normal
    )
    val giantsTitle3 = GiantsTextStyle.copy(
        fontSize = 22.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.Normal
    )
    val giantsTitle4 = GiantsTextStyle.copy(
        fontSize = 18.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Normal
    )
    val giantsTitle5 = GiantsTextStyle.copy(
        fontSize = 16.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Normal
    )
    val giantsTitle6 = GiantsTextStyle.copy(
        fontSize = 12.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.Normal
    )

}
