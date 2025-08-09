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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
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
import com.combo.runcombi.core.designsystem.component.RunCombiSelectableButton
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey03
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title4
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.feature.setting.R
import com.combo.runcombi.setting.model.EditMemberEvent
import com.combo.runcombi.setting.model.EditMemberUiState
import com.combo.runcombi.setting.model.EditPetEvent
import com.combo.runcombi.setting.model.EditPetUiState
import com.combo.runcombi.setting.viewmodel.EditPetViewModel
import com.combo.runcombi.ui.ext.clickableSingle
import com.combo.runcombi.ui.ext.clickableWithoutRipple
import com.combo.runcombi.ui.ext.screenDefaultPadding
import com.combo.runcombi.ui.util.BitmapUtil
import com.combo.runcombi.ui.util.BitmapUtil.resizeBitmap

@Composable
fun EditPetScreen(
    editPetViewModel: EditPetViewModel = hiltViewModel(),
    petId: Int,
    onBack: () -> Unit,
) {
    val uiState by editPetViewModel.uiState.collectAsStateWithLifecycle()
    val profileBitmap by editPetViewModel.profileBitmap.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val eventFlow = editPetViewModel.eventFlow

    LaunchedEffect(Unit) {
        editPetViewModel. getMemberProfile(petId = petId)
    }

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is EditPetEvent.Success -> {
                    onBack()
                }

                is EditPetEvent.Error -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0x80000000)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary01)
        }
    }


    EditPetContent(
        uiState = uiState,
        profileBitmap = profileBitmap,
        onNameChange = { editPetViewModel.onNameChange(it) },
        onSelectStyle = {
            editPetViewModel.onSelectRunStyle(it)
        },
        onAgeChange = {
            editPetViewModel.onAgeChange(it)
        },
        onWeightChange = { editPetViewModel.onWeightChange(it) },
        onProfileImageSelect = { bitmap -> editPetViewModel.setProfileBitmap(bitmap) },
        onCancel = onBack,
        onSave = {
            val file =
                profileBitmap?.let { BitmapUtil.bitmapToFile(context, it, "pet.jpg") }

            editPetViewModel.savePetInfo(file, petId = petId)
        },
        onDelete = {
            editPetViewModel.deletePet(petId = petId)
        }
    )
}

@Composable
fun EditPetContent(
    uiState: EditPetUiState,
    profileBitmap: android.graphics.Bitmap? = null,
    onNameChange: (String) -> Unit = {},
    onAgeChange: (String) -> Unit = {},
    onWeightChange: (String) -> Unit = {},
    onProfileImageSelect: (android.graphics.Bitmap) -> Unit = {},
    onSelectStyle: (RunStyle) -> Unit = {},
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
    onDelete: () -> Unit = {},
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    val albumLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            val resizedBitmap = resizeBitmap(bitmap, 720, 720)
            onProfileImageSelect(resizedBitmap)
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        EditPetAppBar(
            onCancel = onCancel,
            onSave = onSave
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
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
                    drawableResId = R.drawable.default_dog_profile,
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
                modifier = Modifier.padding(bottom = 6.dp),
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
            Text(text = "나이 (살)", style = body1, color = Grey08, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(6.dp))
            RunCombiTextField(
                value = uiState.age,
                maxLength = 3,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                onValueChange = { onAgeChange(it) },
                modifier = Modifier
                    .height(48.dp),
                isError = uiState.isAgeError,
                visualTransformation = VisualTransformation.None,
                enabled = true,
                singleLine = true,
                trailingText = ""
            )
            Spacer(Modifier.height(32.dp))
            Text(
                text = "체중 (kg)",
                style = body1,
                color = Grey08,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
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
                trailingText = ""
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = "산책스타일", style = body2, color = Grey08, modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(5.dp))
            RunCombiSelectableButton(
                text = "에너지가 넘쳐요!",
                modifier = Modifier.height(40.dp),
                isSelected = uiState.runStyle == RunStyle.RUNNING,
                onClick = { onSelectStyle(RunStyle.RUNNING) },
            )
            Spacer(Modifier.height(14.dp))
            RunCombiSelectableButton(
                text = "여유롭게 걸어요",
                modifier = Modifier.height(40.dp),
                isSelected = uiState.runStyle == RunStyle.WALKING,
                onClick = { onSelectStyle(RunStyle.WALKING) },
            )
            Spacer(Modifier.height(14.dp))
            RunCombiSelectableButton(
                text = "천천히 걸으며 자주 쉬어요",
                modifier = Modifier.height(40.dp),
                isSelected = uiState.runStyle == RunStyle.SLOW_WALKING,
                onClick = { onSelectStyle(RunStyle.SLOW_WALKING) },
            )
            Spacer(Modifier.height(38.dp))
            if (uiState.isRemovable)
                Text(
                    text = "반려견 삭제", 
                    color = Grey06, 
                    style = body3.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline), 
                    modifier = Modifier.clickableSingle {
                        onDelete()
                    }
                )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun EditPetAppBar(
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
                text = "콤비 정보 수정",
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
fun EditPetContentPreview() {
    val previewUiState = EditPetUiState(
        name = "멍멍이",
        age = "3",
        weight = "15",
        runStyle = RunStyle.RUNNING,
        profileImageUrl = "",
        isNameError = false,
        isAgeError = false,
        isWeightError = false,
        isLoading = false,
        isRemovable = true
    )

    EditPetContent(
        uiState = previewUiState,
        profileBitmap = null,
        onNameChange = {},
        onAgeChange = {},
        onWeightChange = {},
        onProfileImageSelect = {},
        onSelectStyle = {},
        onCancel = {},
        onSave = {},
        onDelete = {}
    )
}