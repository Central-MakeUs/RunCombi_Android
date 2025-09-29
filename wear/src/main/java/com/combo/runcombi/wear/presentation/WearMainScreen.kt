package com.combo.runcombi.wear.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.domain.user.model.UserInfo
import com.combo.runcombi.wear.presentation.theme.RunCombi_AndroidTheme

@Composable
fun WearMainScreen(
    userInfo: UserInfo,
    onStartExercise: () -> Unit = {},
    onRequestLocationPermission: () -> Unit = {},
) {
    val context = LocalContext.current
    val listState = rememberScalingLazyListState()
    
    var hasLocationPermission by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        fun checkPermissions(): Boolean {
            val hasFineLocation = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            val hasCoarseLocation = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            return hasFineLocation || hasCoarseLocation
        }
        
        // 초기 권한 체크
        hasLocationPermission = checkPermissions()
        android.util.Log.d("WearMainScreen", "초기 권한 상태: $hasLocationPermission")
        
        // 권한이 없을 때만 주기적으로 체크
        while (!hasLocationPermission) {
            kotlinx.coroutines.delay(2000)
            val newPermissionState = checkPermissions()
            android.util.Log.d("WearMainScreen", "권한 재체크: $newPermissionState")
            if (newPermissionState != hasLocationPermission) {
                android.util.Log.d("WearMainScreen", "권한 상태 변경: $hasLocationPermission -> $newPermissionState")
                hasLocationPermission = newPermissionState
                break
            }
        }
    }
    
    ScalingLazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
    ) {
        item {
            Text(
                text = userInfo.member.nickname,
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center
            )
        }
        
        item {
            Text(
                text = "콤비 ${userInfo.petList.size}마리",
                style = MaterialTheme.typography.body2,
                textAlign = TextAlign.Center
            )
        }
        
        if (!hasLocationPermission) {
            item {
                Text(
                    text = "위치 권한이 필요합니다",
                    style = MaterialTheme.typography.caption1,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            item {
                RunCombiButton(
                    text = "위치 권한 허용",
                    onClick = onRequestLocationPermission,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                )
            }
        } else {
            item {
                RunCombiButton(
                    text = "운동 시작",
                    onClick = onStartExercise,
                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
                )
            }
        }
        
    }
}

@Composable
@Preview(device = WearDevices.SMALL_ROUND)
fun WearMainScreenPreview() {
    RunCombi_AndroidTheme {
        WearMainScreen(
            userInfo = UserInfo(
                member = Member(
                    nickname = "창스",
                    gender = Gender.MALE,
                    height = 180,
                    weight = 80,
                    profileImageUrl = ""
                ),
                petList = listOf(
                    Pet(
                        id = 1,
                        name = "초코",
                        age = 10,
                        weight = 5.5,
                        runStyle = RunStyle.RUNNING,
                        profileImageUrl = ""
                    ),
                    Pet(
                        id = 2,
                        name = "코난",
                        age = 8,
                        weight = 6.5,
                        runStyle = RunStyle.RUNNING,
                        profileImageUrl = ""
                    )
                ),
                memberStatus = MemberStatus.LIVE
            )
        )
    }
}
