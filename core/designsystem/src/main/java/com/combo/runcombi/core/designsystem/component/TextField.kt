package com.combo.runcombi.core.designsystem.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey07
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3

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
    trailingText: String? = null,
    maxLength: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    TextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChange(it)
            }
        },
        placeholder = {
            Text(
                text = placeholder,
                color = Grey06,
                style = body3
            )
        },
        singleLine = singleLine,
        enabled = enabled,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .border(
                width = 1.dp,
                color = if (isError) Color(0xFFFC5555) else Color.Transparent,
                shape = RoundedCornerShape(6.dp)
            ),
        trailingIcon = trailingText?.let {
            {
                Text(
                    text = it,
                    color = Grey07,
                    style = body3
                )
            }
        },
        textStyle = body3,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Grey04,
            unfocusedContainerColor = Grey04,
            disabledContainerColor = Grey04,
            errorContainerColor = Grey04,
            focusedTextColor = if (isError) Color(0xFFFC5555) else Grey08,
            unfocusedTextColor = if (isError) Color(0xFFFC5555) else Grey06,
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