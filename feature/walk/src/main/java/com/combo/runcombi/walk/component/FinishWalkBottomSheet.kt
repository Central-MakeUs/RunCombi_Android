package com.combo.runcombi.walk.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishWalkBottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    onFinish: () -> Unit,
    onResume: () -> Unit,
) {
    if (show) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Color(0xFF232323)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF232323))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("오늘 운동은 여기서 마무리할까요?", color = Color.White, fontSize = 20.sp)
                Spacer(Modifier.height(8.dp))
                Text("종료하면 지금까지의 기록이 저장됩니다.", color = Color(0xFFBDBDBD), fontSize = 14.sp)
                Spacer(Modifier.height(24.dp))
                Row {
                    Button(
                        onClick = onFinish,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF444444)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("종료", color = Color.White)
                    }
                    Spacer(Modifier.width(16.dp))
                    Button(
                        onClick = onResume,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD9FF4B)),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("아니요", color = Color.Black)
                    }
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
} 