package com.combo.runcombi.walk.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LocationPermissionDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("정확한 위치 권한 필요") },
            text = {
                Text("운동 기능을 사용하려면 정확한 위치 권한이 필요합니다.\n설정에서 권한을 허용해 주세요.")
            },
            confirmButton = {
                Button(onClick = onConfirm) {
                    Text("설정으로 이동")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("취소")
                }
            }
        )
    }
} 