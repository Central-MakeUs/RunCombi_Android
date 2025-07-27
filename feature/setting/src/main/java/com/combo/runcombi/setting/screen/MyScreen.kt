package com.combo.runcombi.setting.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.feature.setting.R
import com.combo.runcombi.core.designsystem.component.NetworkImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey03
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.Primary02
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title4
import com.combo.runcombi.setting.viewmodel.MyViewModel
import com.combo.runcombi.ui.ext.clickableSingle

@Composable
fun MyScreen(
    onClickSetting: () -> Unit = {},
    viewModel: MyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingContent(
        member = uiState.member,
        petList = uiState.petList,
        onClickSetting = onClickSetting,
        onEditProfile = {},
        onAddPet = {},
        onEditPet = {}
    )
}

@Composable
fun SettingContent(
    member: Member?,
    petList: List<Pet>,
    modifier: Modifier = Modifier,
    onClickSetting: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onAddPet: () -> Unit = {},
    onEditPet: (Pet) -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(20.dp)
                        .size(24.dp)
                        .clickableSingle {
                            onClickSetting()
                        }

                )
            }
            Spacer(Modifier.height(32.dp))
            MemberProfile(
                member = member,
            )
            Spacer(modifier = Modifier.height(19.dp))
            Text(
                text = member?.nickname ?: "",
                color = Color.White,
                style = title4
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onEditProfile,
                shape = RoundedCornerShape(6.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp),
                border = BorderStroke(width = 1.dp, color = Grey05)
            ) {
                Text("내 정보 수정", style = body3, color = Grey05)
            }
            Spacer(modifier = Modifier.height(32.dp))
            PetCardList(
                petList = petList,
                onEditPet = onEditPet,
                onAddPet = onAddPet,
                modifier = Modifier
                    .padding(bottom = 80.dp, start = 20.dp, end = 20.dp)
            )
        }

    }
}

@Composable
fun MemberProfile(
    member: Member?,
) {
    Box(
        modifier = Modifier
            .size(89.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Primary01)
    ) {
        NetworkImage(
            contentScale = ContentScale.Crop,
            imageUrl = member?.profileImageUrl ?: "",
            drawableResId = R.drawable.person_profile,
            modifier = Modifier
                .fillMaxSize(),
        )
    }
}

@Composable
fun PetCard(
    pet: Pet,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .size(154.dp)
            .background(Grey02, shape = RoundedCornerShape(6.dp))
            .clickable { onEdit() }
            .padding(8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Primary02)
            ) {
                NetworkImage(
                    imageUrl = pet.profileImageUrl,
                    drawableResId = R.drawable.ic_pet_defalut,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = pet.name,
                color = Color.White,
                style = title4
            )
        }
    }
}

@Composable
fun PetCardList(
    petList: List<Pet>,
    onEditPet: (Pet) -> Unit,
    onAddPet: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        petList.forEach { pet ->
            PetCard(
                pet = pet,
                onEdit = { onEditPet(pet) }
            )
        }
        if (petList.size == 1) {
            Box(
                modifier = Modifier
                    .size(154.dp)
                    .border(
                        border = BorderStroke(width = 1.dp, color = Grey03),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable { onAddPet() }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingContentPreview() {
    SettingContent(
        member = Member(
            nickname = "홍길동",
            gender = Gender.MALE,
            height = 170,
            weight = 65,
            profileImageUrl = null
        ),
        petList = listOf(
            Pet(
                id = 1,
                name = "초코",
                weight = 5.0,
                age = 3,
                profileImageUrl = null,
                runStyle = RunStyle.WALKING
            ),
        )
    )
}