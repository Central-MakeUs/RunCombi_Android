package com.combo.runcombi.wear.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    onViewHistory: () -> Unit = {},
    onSettings: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // 환영 메시지
        Text(
            text = "안녕하세요!",
            textAlign = TextAlign.Center
        )
        
        Text(
            text = userInfo.member.nickname,
            textAlign = TextAlign.Center
        )
        
        Text(
            text = "콤비 ${userInfo.petList.size}마리와 함께",
            style = MaterialTheme.typography.body2,
            textAlign = TextAlign.Center
        )
        
        // 운동 시작 버튼
        RunCombiButton(
            text = "운동 시작",
            onClick = onStartExercise,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        // 운동 기록 보기 버튼
        RunCombiButton(
            text = "운동 기록",
            onClick = onViewHistory,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        
        // 설정 버튼
        RunCombiButton(
            text = "설정",
            onClick = onSettings,
            modifier = Modifier.padding(vertical = 4.dp)
        )
        
        // 추가 정보
        Text(
            text = "오늘도 화이팅!",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
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
