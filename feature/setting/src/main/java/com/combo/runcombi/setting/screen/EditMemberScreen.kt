package com.combo.runcombi.setting.screen

import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.NetworkImage
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey03
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.Primary03
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title4
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.feature.setting.R
import com.combo.runcombi.setting.model.EditMemberEvent
import com.combo.runcombi.setting.viewmodel.EditMemberViewModel
import com.combo.runcombi.setting.model.EditMemberUiState
import com.combo.runcombi.ui.ext.clickableWithoutRipple
import com.combo.runcombi.ui.ext.screenDefaultPadding
import com.combo.runcombi.ui.util.BitmapUtil
import com.combo.runcombi.ui.util.BitmapUtil.resizeBitmap

@Composable
fun EditMemberScreen(
    onBack: () -> Unit,
    editMemberViewModel: EditMemberViewModel = hiltViewModel(),
) {
    val uiState by editMemberViewModel.uiState.collectAsStateWithLifecycle()
    val profileBitmap by editMemberViewModel.profileBitmap.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val eventFlow = editMemberViewModel.eventFlow

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is EditMemberEvent.Success -> {
                    onBack()
                }

                is EditMemberEvent.Error -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary01)
        }
    }

    EditMemberContent(
        uiState = uiState,
        profileBitmap = profileBitmap,
        onNameChange = { editMemberViewModel.onNameChange(it) },
        onHeightChange = { editMemberViewModel.onHeightChange(it) },
        onWeightChange = { editMemberViewModel.onWeightChange(it) },
        onGenderSelect = { editMemberViewModel.selectGender(it) },
        onProfileImageSelect = { bitmap -> editMemberViewModel.setProfileBitmap(bitmap) },
        onCancel = onBack,
        onSave = {
            val file =
                profileBitmap?.let { BitmapUtil.bitmapToFile(context, it, "profile.jpg") }

            editMemberViewModel.saveMemberInfo(file)
        }
    )
}

@Composable
fun EditMemberContent(
    uiState: EditMemberUiState,
    profileBitmap: android.graphics.Bitmap?,
    onNameChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onWeightChange: (String) -> Unit,
    onGenderSelect: (Gender) -> Unit,
    onProfileImageSelect: (android.graphics.Bitmap) -> Unit,
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    val albumLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            val resizedBitmap = resizeBitmap(bitmap, 300, 300)
            onProfileImageSelect(resizedBitmap)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        EditMemberAppBar(
            onCancel = onCancel,
            onSave = onSave
        )

        Column(
            modifier = Modifier
                .clickableWithoutRipple {
                    keyboardController?.hide()
                    localFocusManager.clearFocus()
                }
                .screenDefaultPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .size(100.dp)
            ) {
                profileBitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "프로필 이미지",
                        modifier = Modifier
                            .size(89.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        contentScale = ContentScale.Crop
                    )
                } ?: NetworkImage(
                    drawableResId = R.drawable.default_person_profile,
                    contentScale = ContentScale.Crop,
                    imageUrl = uiState.profileImageUrl,
                    modifier = Modifier
                        .size(89.dp)
                        .clip(RoundedCornerShape(4.dp)),
                )

                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        localFocusManager.clearFocus()
                        albumLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                    }, modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 20.dp, y = 20.dp)
                ) {
                    StableImage(
                        drawableResId = R.drawable.camera_setting, modifier = Modifier.size(32.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "이름", style = body1, color = Grey08)
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "한글 5자 / 영문 7자 이하",
                    textAlign = TextAlign.End,
                    style = body3,
                    color = Grey06
                )
            }

            RunCombiTextField(
                value = uiState.name,
                onValueChange = { onNameChange(it) },
                placeholder = "런콤비",
                isError = uiState.isNameError,
                visualTransformation = VisualTransformation.None,
                enabled = true,
                singleLine = true,
            )
            Spacer(Modifier.height(32.dp))
            Text(text = "성별", style = body1, color = Grey08, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(6.dp))
            Row {
                RunCombiButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onGenderSelect(Gender.MALE)
                    },
                    textColor = if (uiState.gender == Gender.MALE) Grey03 else WhiteFF,
                    enabledColor = if (uiState.gender == Gender.MALE) Primary01 else Grey04,
                    text = "남성",
                )
                Spacer(Modifier.width(12.dp))
                RunCombiButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        onGenderSelect(Gender.FEMALE)
                    },
                    textColor = if (uiState.gender == Gender.FEMALE) Grey03 else WhiteFF,
                    enabledColor = if (uiState.gender == Gender.FEMALE) Primary01 else Grey04,
                    text = "여성",
                )
            }
            Spacer(Modifier.height(32.dp))
            Text(text = "키", style = body1, color = Grey08, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            RunCombiTextField(
                value = uiState.height,
                maxLength = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                onValueChange = { onHeightChange(it) },
                modifier = Modifier
                    .height(48.dp),
                isError = uiState.isHeightError,
                visualTransformation = VisualTransformation.None,
                enabled = true,
                singleLine = true,
                trailingText = "cm"
            )
            Spacer(Modifier.height(32.dp))
            Text(text = "몸무게", style = body1, color = Grey08, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            RunCombiTextField(
                value = uiState.weight,
                maxLength = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                onValueChange = { onWeightChange(it) },
                modifier = Modifier
                    .height(48.dp),
                isError = uiState.isWeightError,
                visualTransformation = VisualTransformation.None,
                enabled = true,
                singleLine = true,
                trailingText = "kg"
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun EditMemberAppBar(
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(Grey01)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "취소",
                style = title4,
                color = Grey05,
                modifier = Modifier.clickableWithoutRipple { onCancel() }
            )

            Text(
                text = "내 정보 수정",
                style = title2,
                color = WhiteFF,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "저장",
                style = title4,
                color = Primary01,
                modifier = Modifier.clickableWithoutRipple { onSave() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditMemberContentPreview() {
    val previewUiState = EditMemberUiState(
        name = "런콤비",
        height = "170",
        weight = "65",
        gender = Gender.MALE,
        isNameError = false,
        isHeightError = false,
        isWeightError = false,
        isLoading = false
    )

    EditMemberContent(
        uiState = previewUiState,
        profileBitmap = null,
        onNameChange = {},
        onHeightChange = {},
        onWeightChange = {},
        onGenderSelect = {},
        onProfileImageSelect = {},
        onCancel = {},
        onSave = {}
    )
}