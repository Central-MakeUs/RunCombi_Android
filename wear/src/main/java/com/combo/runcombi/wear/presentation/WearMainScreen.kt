package com.combo.runcombi.wear.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
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
) {
    val listState = rememberScalingLazyListState()
    
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
        
        item {
            // 운동 시작 버튼
            RunCombiButton(
                text = "운동 시작",
                onClick = onStartExercise,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp)
            )
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
