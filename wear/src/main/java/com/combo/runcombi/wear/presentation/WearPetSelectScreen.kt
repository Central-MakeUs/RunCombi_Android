package com.combo.runcombi.wear.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.combo.runcombi.core.designsystem.component.NetworkImage
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.UserInfo
import com.combo.runcombi.wear.presentation.theme.RunCombi_AndroidTheme

data class PetUiModel(
    val pet: Pet,
    val isSelected: Boolean = false,
    val originIndex: Int,
    val selectedOrder: Int? = null,
)

@Composable
fun WearPetSelectScreen(
    userInfo: UserInfo,
    onStartExercise: (List<Pet>) -> Unit,
    onBack: () -> Unit,
) {
    var petUiList by remember {
        mutableStateOf(
            userInfo.petList.mapIndexed { idx, pet ->
                PetUiModel(pet = pet, isSelected = false, originIndex = idx)
            })
    }

    val selectedPets = petUiList.filter { it.isSelected }.map { it.pet }

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
                text = "콤비 선택",
                style = MaterialTheme.typography.title2,
                color = Grey08,
                textAlign = TextAlign.Center
            )
        }

        item {
            Text(
                text = userInfo.member.nickname,
                style = MaterialTheme.typography.body1,
                color = Primary01,
                textAlign = TextAlign.Center
            )
        }

        // 펫 목록
        items(petUiList) { petUi ->
            PetSelectItem(
                petUi = petUi, onClick = {
                    petUiList = togglePetSelection(petUiList, petUi.pet)
                })
        }

        item {
            // 선택된 펫 표시
            if (selectedPets.isNotEmpty()) {
                Text(
                    text = "${selectedPets.map { it.name }.joinToString()} 선택됨",
                    style = MaterialTheme.typography.body2,
                    color = Primary01,
                    textAlign = TextAlign.Center
                )
            }
        }

        item {
            // 시작 버튼
            RunCombiButton(
                text = "시작",
                onClick = { onStartExercise(selectedPets) },
                modifier = Modifier.padding(horizontal = 16.dp),
                enabled = selectedPets.isNotEmpty()
            )
        }

        item {
            // 뒤로가기 버튼
            RunCombiButton(
                text = "뒤로",
                onClick = onBack,
                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp)
            )
        }
    }
}

@Composable
private fun MemberProfile(member: Member) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 유저 아바타 (간단한 원형 배경)
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Primary01, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = member.nickname.take(1),
                color = Color.White,
                style = MaterialTheme.typography.body2
            )
        }

        Text(
            text = member.nickname, style = MaterialTheme.typography.body1, color = Grey08
        )
    }
}

@Composable
private fun PetSelectItem(
    petUi: PetUiModel,
    onClick: () -> Unit,
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .background(
            if (petUi.isSelected) Primary01.copy(alpha = 0.1f) else Color.Transparent,
            RoundedCornerShape(4.dp)
        )
        .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        // 펫 정보 - 간단하게
        Text(
            text = petUi.pet.name,
            style = MaterialTheme.typography.body2,
            color = if (petUi.isSelected) Primary01 else Grey08,
            modifier = Modifier.weight(1f)
        )

        // 선택 표시
        if (petUi.isSelected) {
            Text(
                text = "✓", color = Primary01, style = MaterialTheme.typography.body1
            )
        }
    }
}

private fun togglePetSelection(
    petUiList: List<PetUiModel>,
    pet: Pet,
): List<PetUiModel> {
    val selectedList =
        petUiList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }
    val tapped = petUiList.find { it.pet == pet } ?: return petUiList

    return if (tapped.isSelected) {
        // 선택 해제
        deselectPet(petUiList, pet)
    } else {
        // 선택 (최대 2마리)
        if (selectedList.size >= 2) return petUiList
        selectPet(petUiList, pet, selectedList.size)
    }
}

private fun deselectPet(petUiList: List<PetUiModel>, pet: Pet): List<PetUiModel> {
    val newList = petUiList.map {
        if (it.pet == pet) it.copy(isSelected = false, selectedOrder = null)
        else it
    }
    val remainSelected =
        newList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }
    return if (remainSelected.isNotEmpty()) {
        newList.map {
            if (it.isSelected) {
                val idx = remainSelected.indexOfFirst { sel -> sel.pet == it.pet }
                it.copy(selectedOrder = idx)
            } else it
        }
    } else {
        newList.map { it.copy(selectedOrder = null) }
    }
}

private fun selectPet(petUiList: List<PetUiModel>, pet: Pet, nextOrder: Int): List<PetUiModel> =
    petUiList.map {
        if (it.pet == pet) it.copy(isSelected = true, selectedOrder = nextOrder)
        else it
    }

@Preview(device = WearDevices.SMALL_ROUND)
@Composable
fun WearPetSelectScreenPreview() {
    RunCombi_AndroidTheme {
        val sampleUserInfo = UserInfo(
            member = Member(
                nickname = "테스트유저",
                gender = com.combo.runcombi.domain.user.model.Gender.MALE,
                height = 175,
                weight = 70,
                profileImageUrl = ""
            ), petList = listOf(
                Pet(
                    id = 1,
                    name = "멍멍이",
                    age = 3,
                    weight = 25.0,
                    runStyle = com.combo.runcombi.domain.user.model.RunStyle.RUNNING,
                    profileImageUrl = ""
                ), Pet(
                    id = 2,
                    name = "야옹이",
                    age = 2,
                    weight = 4.5,
                    runStyle = com.combo.runcombi.domain.user.model.RunStyle.WALKING,
                    profileImageUrl = ""
                )
            ), memberStatus = com.combo.runcombi.domain.user.model.MemberStatus.LIVE
        )

        WearPetSelectScreen(userInfo = sampleUserInfo, onStartExercise = {}, onBack = {})
    }
}
