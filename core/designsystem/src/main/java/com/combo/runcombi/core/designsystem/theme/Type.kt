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

    private val heading_xl = DefaultTextStyle.copy(
        fontSize = 60.sp,
        lineHeight = 78.sp
    )

    val heading_xl_bold = heading_xl.copy(
        fontWeight = FontWeight.Bold,
    )

    val heading_xl_regular = heading_xl.copy(
        fontWeight = FontWeight.Normal,
    )

    private val heading_lg = DefaultTextStyle.copy(
        fontSize = 48.sp,
        lineHeight = 64.sp
    )

    val heading_lg_bold = heading_lg.copy(
        fontWeight = FontWeight.Bold
    )

    val heading_lg_regular = heading_lg.copy(
        fontWeight = FontWeight.Normal
    )

    private val heading_md = DefaultTextStyle.copy(
        fontSize = 34.sp,
        lineHeight = 48.sp
    )

    val heading_md_bold = heading_md.copy(
        fontWeight = FontWeight.Bold
    )

    val heading_md_regular = heading_md.copy(
        fontWeight = FontWeight.Normal
    )

    private val heading_sm = DefaultTextStyle.copy(
        fontSize = 30.sp,
        lineHeight = 40.sp
    )

    val heading_sm_bold = heading_sm.copy(
        fontWeight = FontWeight.Bold
    )

    val heading_sm_regular = heading_sm.copy(
        fontWeight = FontWeight.Normal
    )

    private val title_xl = DefaultTextStyle.copy(
        fontSize = 24.sp,
        lineHeight = 34.sp
    )

    val title_xl_bold = title_xl.copy(
        fontWeight = FontWeight.Bold
    )

    val title_xl_regular = title_xl.copy(
        fontWeight = FontWeight.Normal
    )

    private val title_lg = DefaultTextStyle.copy(
        fontSize = 20.sp,
        lineHeight = 30.sp
    )

    val title_lg_bold = title_lg.copy(
        fontWeight = FontWeight.Bold
    )

    val title_lg_regular = title_lg.copy(
        fontWeight = FontWeight.Normal
    )

    private val body_xl = DefaultTextStyle.copy(
        fontSize = 18.sp,
        lineHeight = 28.sp
    )

    val body_xl_bold = body_xl.copy(
        fontWeight = FontWeight.Bold
    )

    val body_xl_regular = body_xl.copy(
        fontWeight = FontWeight.Normal
    )

    private val body_lg = DefaultTextStyle.copy(
        fontSize = 16.sp,
        lineHeight = 24.sp
    )

    val body_lg_bold = body_lg.copy(
        fontWeight = FontWeight.Bold
    )

    val body_lg_regular = body_lg.copy(
        fontWeight = FontWeight.Normal
    )

    private val body_md = DefaultTextStyle.copy(
        fontSize = 14.sp,
        lineHeight = 20.sp
    )

    val body_md_bold = body_md.copy(
        fontWeight = FontWeight.Bold
    )

    val body_md_regular = body_md.copy(
        fontWeight = FontWeight.Normal
    )

    private val body_sm = DefaultTextStyle.copy(
        fontSize = 12.sp,
        lineHeight = 18.sp
    )

    val body_sm_bold = body_sm.copy(
        fontWeight = FontWeight.Bold
    )

    val body_sm_regular = body_sm.copy(
        fontWeight = FontWeight.Normal
    )

    private val tab = DefaultTextStyle.copy(
        fontSize = 11.sp
    )

    val tab_regular = tab.copy(
        fontWeight = FontWeight.Normal
    )
}
