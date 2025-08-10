package com.combo.runcombi.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey07
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunCombiBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    title: String,
    subtitle: String,
    acceptButtonText: String,
    cancelButtonText: String,
) {
    if (show) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Grey02,
            dragHandle = null,
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color(0xFF232323))
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(title, color = Color.White, style = title2)
                Spacer(Modifier.height(10.dp))
                Text(subtitle, color = Grey07, style = body1)
                Spacer(Modifier.height(32.dp))
                Row {
                    RunCombiButton(
                        text = cancelButtonText,
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        enabledColor = Grey04,
                        textColor = Grey08,
                    )
                    Spacer(Modifier.width(10.dp))
                    RunCombiButton(
                        text = acceptButtonText,
                        onClick = onAccept,
                        modifier = Modifier.weight(1f),
                        enabledColor = Primary01,
                        textColor = Grey02,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunCombiDeleteBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    onAccept: () -> Unit,
    onCancel: () -> Unit,
    title: String,
    subtitle: String,
    acceptButtonText: String,
    acceptButtonTextColor: Color = Color.White,
    acceptButtonBackgroundColor: Color = Color(0xFFFC5555),
    cancelButtonText: String,
) {
    if (show) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Grey02,
            dragHandle = null,
            shape = RoundedCornerShape(
                topStart = 20.dp,
                topEnd = 20.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(Color(0xFF232323))
                    .padding(horizontal = 20.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(title, color = Color.White, style = title2)
                Spacer(Modifier.height(10.dp))
                Text(subtitle, color = Grey07, style = body1)
                Spacer(Modifier.height(32.dp))
                Row {
                    RunCombiButton(
                        text = acceptButtonText,
                        onClick = onAccept,
                        modifier = Modifier.weight(1f),
                        enabledColor = acceptButtonBackgroundColor,
                        textColor = acceptButtonTextColor,
                    )
                    Spacer(Modifier.width(10.dp))
                    RunCombiButton(
                        text = cancelButtonText,
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        enabledColor = Grey04,
                        textColor = Grey08,
                    )
                }
            }
        }
    }
}
