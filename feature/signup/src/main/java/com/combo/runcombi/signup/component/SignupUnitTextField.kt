package com.combo.runcombi.signup.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SignupUnitTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    placeholder: String = "",
    unit: String = "",
    isError: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    singleLine: Boolean = true,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
                    .border(
                        width = 2.dp,
                        color = if (isError) Color(0xFFFF4D4F) else Color.Transparent,
                        shape = RoundedCornerShape(8.dp)
                    ),
                singleLine = singleLine,
                enabled = enabled,
                visualTransformation = visualTransformation,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF3A3A3A),
                    unfocusedContainerColor = Color(0xFF3A3A3A),
                    disabledContainerColor = Color(0xFF3A3A3A),
                    errorContainerColor = Color(0xFF3A3A3A),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    errorTextColor = Color(0xFFFF4D4F),
                    cursorColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )
            if (unit.isNotEmpty()) {
                Text(
                    text = unit,
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = Color(0xFFFF4D4F),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignupUnitTextField() {
    SignupUnitTextField(
        value = "",
        onValueChange = {},
        title = "나이",
        placeholder = "나이를 입력하세요",
        unit = "세"
    )
} 