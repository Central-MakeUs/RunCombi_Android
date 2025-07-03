package com.combo.runcombi.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title4

@Composable
fun RunCombiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    enabledColor: Color = Color(0xFFD7FE63),
    disabledColor: Color = Color(0xFF353434),
    textColor: Color = Color(0xFF090909),
    disabledTextColor: Color = Color(0xFF090909),
    cornerRadius: Int = 6,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(cornerRadius.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = enabledColor,
            disabledContainerColor = disabledColor,
            contentColor = textColor,
            disabledContentColor = disabledTextColor
        )
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            style = title4
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRunCombiButton() {
    RunCombiButton(text = "확인", onClick = {})
} 