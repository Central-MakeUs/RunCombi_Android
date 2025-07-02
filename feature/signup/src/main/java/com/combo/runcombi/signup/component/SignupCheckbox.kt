package com.combo.runcombi.signup.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SignupCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    checkedColor: Color = Color(0xFFD7FE63),
    uncheckedColor: Color = Color(0xFF3A3A3A),
    checkmarkColor: Color = Color.Black,
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .background(
                color = if (checked) checkedColor else uncheckedColor,
                shape = RoundedCornerShape(6.dp)
            )
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                painter = painterResource(android.R.drawable.checkbox_on_background), // 임시 체크 아이콘
                contentDescription = "Checked",
                tint = checkmarkColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignupCheckbox() {
    SignupCheckbox(checked = true, onCheckedChange = {})
} 