package com.combo.runcombi.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey07
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3

@OptIn(ExperimentalMaterial3Api::class)
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
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Grey04,
        unfocusedContainerColor = Grey04,
        disabledContainerColor = Grey04,
        errorContainerColor = Grey04,

        focusedTextColor = if (isError) Color(0xFFFC5555) else Grey08,
        unfocusedTextColor = if (isError) Color(0xFFFC5555) else Grey06,
        disabledTextColor = Grey06,
        errorTextColor = Color(0xFFFC5555),

        cursorColor = Color(0xFFD7FE63),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent
    )

    BasicTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChange(it)
            }
        },
        cursorBrush = SolidColor(Primary01),
        enabled = enabled,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        textStyle = body1.copy(color = if (isError) Color(0xFFFC5555) else Grey08),
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .border(
                width = 1.dp,
                color = if (isError) Color(0xFFFC5555) else Color.Transparent,
                shape = RoundedCornerShape(6.dp)
            )
            .background(Grey04),
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                visualTransformation = visualTransformation,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = placeholder,
                        color = Grey06,
                        style = body3
                    )
                },
                enabled = enabled,
                singleLine = singleLine,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = contentPadding,
                colors = textFieldColors,
                trailingIcon = trailingText?.let {
                    {
                        Text(
                            text = it,
                            color = Grey07,
                            style = body1
                        )
                    }
                },
            )
        }
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