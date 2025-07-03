package com.combo.runcombi.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.combo.runcombi.core.designsystem.R

@Composable
fun RunCombiCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedColor: Color = Color(0xFFD7FE63),
    uncheckedColor: Color = Color(0xFF292929),
) {
    Box(
        modifier = modifier
            .size(20.dp)
            .background(
                color = if (checked) checkedColor else uncheckedColor,
                shape = RoundedCornerShape(2.dp)
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            StableImage(
                drawableResId = R.drawable.ic_check,
                modifier = Modifier
                    .height(10.83.dp)
                    .width(14.58.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRunCombiCheckbox() {
    RunCombiCheckbox(checked = true, onCheckedChange = {})
} 