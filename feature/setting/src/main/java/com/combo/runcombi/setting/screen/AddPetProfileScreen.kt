package com.combo.runcombi.setting.screen

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.feature.setting.R
import com.combo.runcombi.setting.model.PetProfileData
import com.combo.runcombi.setting.viewmodel.AddPetViewModel
import com.combo.runcombi.setting.viewmodel.PetProfileViewModel
import com.combo.runcombi.ui.ext.clickableWithoutRipple
import com.combo.runcombi.ui.ext.screenDefaultPadding
import com.combo.runcombi.ui.util.BitmapUtil
import com.combo.runcombi.ui.util.BitmapUtil.resizeBitmap

@Composable
fun AddPetProfileScreen(
    onNext: () -> Unit,
    addPetViewModel: AddPetViewModel,
    petProfileViewModel: PetProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    val uiState by petProfileViewModel.uiState.collectAsStateWithLifecycle()
    val profileBitmap by petProfileViewModel.profileBitmap.collectAsStateWithLifecycle()

    val albumLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            val resizedBitmap = resizeBitmap(bitmap, 300, 300)
            petProfileViewModel.setProfileBitmap(resizedBitmap)
        }
    }

    Column(
        modifier = Modifier
            .background(Grey01)
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
                .padding(top = 38.dp, bottom = 26.dp)
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
            } ?: StableImage(
                drawableResId = R.drawable.default_dog_profile,
                modifier = Modifier.size(89.dp)
            )

            IconButton(
                onClick = {
                    keyboardController?.hide()
                    localFocusManager.clearFocus()
                    albumLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 20.dp, y = 20.dp)
            ) {
                StableImage(
                    drawableResId = R.drawable.camera_setting,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.size(26.dp))

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
                style = body3, color = Grey06
            )
        }

        RunCombiTextField(
            value = uiState.name,
            onValueChange = { petProfileViewModel.onNameChange(it) },
            placeholder = "런콤비",
            isError = uiState.isError,
            visualTransformation = VisualTransformation.None,
            enabled = true,
            singleLine = true,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text("다른 반려견도 나중에 추가할 수 있어요", style = body3, color = Grey06)

        Spacer(modifier = Modifier.height(32.dp))

        RunCombiButton(
            onClick = {
                keyboardController?.hide()
                localFocusManager.clearFocus()

                petProfileViewModel.validateAndProceed {
                    val file = profileBitmap?.let {
                        BitmapUtil.bitmapToFile(
                            context,
                            it,
                            "pet_profile.jpg"
                        )
                    }
                    addPetViewModel.setPetProfile(
                        PetProfileData(
                            name = uiState.name,
                            profileFile = file
                        )
                    )
                    onNext()
                }
            },
            text = "다음",
            enabled = uiState.isButtonEnabled,
        )
    }
}
