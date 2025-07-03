package com.combo.runcombi.core.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08

@Composable
fun RunCombiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    singleLine: Boolean = true,
) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Grey06) },
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isError) Color(0xFFFC5555) else Color.Transparent,
                shape = RoundedCornerShape(6.dp)
            ),
        singleLine = singleLine,
        enabled = enabled,
        visualTransformation = visualTransformation,
        colors = TextFieldDefaults.colors(
            focusedContainerColor =  Grey04,
            unfocusedContainerColor =  Grey04,
            disabledContainerColor =  Grey04,
            errorContainerColor = Grey04,
            focusedTextColor =  Grey08,
            unfocusedTextColor = Grey06,
            errorTextColor = Color(0xFFFC5555),
            cursorColor = Color(0xFFD7FE63),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewRunCombiTextField() {
    RunCombiTextField(
        value = "",
        onValueChange = {},
        placeholder = "이름을 입력하세요"
    )
} 