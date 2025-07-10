package com.combo.runcombi.signup.screen

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.ext.clickableWithoutRipple
import com.combo.runcombi.core.designsystem.ext.screenDefaultPadding
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.feature.signup.R
import com.combo.runcombi.signup.model.PermissionType
import com.combo.runcombi.signup.model.ProfileData
import com.combo.runcombi.signup.model.ProfileEvent
import com.combo.runcombi.signup.viewmodel.ProfileViewModel
import com.combo.runcombi.signup.viewmodel.SignupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onNext: () -> Unit,
    signupViewModel: SignupViewModel = hiltViewModel(),
    profileViewModel: ProfileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    val uiState by profileViewModel.uiState.collectAsState()
    val profileBitmap by profileViewModel.profileBitmap.collectAsState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val albumLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            profileViewModel.onImageSelected(bitmap)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        bitmap?.let { profileViewModel.onImageSelected(it) }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) profileViewModel.openCamera()
        else profileViewModel.onPermissionDenied(PermissionType.CAMERA)
    }

    LaunchedEffect(true) {
        profileViewModel.eventFlow.collect { event ->
            when (event) {
                is ProfileEvent.ShowImagePickerBottomSheet -> showBottomSheet = true
                is ProfileEvent.RequestCameraPermission ->
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)

                is ProfileEvent.OpenAlbum ->
                    albumLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))

                is ProfileEvent.OpenCamera ->
                    cameraLauncher.launch(null)

                else -> Unit
            }
        }
    }

    LaunchedEffect(true) {
        signupViewModel.clearProfile()
    }

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
                .padding(top = 29.dp, bottom = 26.dp)
                .size(120.dp)
        ) {
            profileBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "프로필 이미지",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } ?: StableImage(
                drawableResId = R.drawable.person_profile,
                modifier = Modifier.size(100.dp)
            )

            IconButton(
                onClick = {
                    keyboardController?.hide()
                    localFocusManager.clearFocus()

                    // profileViewModel.onCameraButtonClick()
                    profileViewModel.openAlbum()
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 8.dp, y = 8.dp)
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
            Text(text = stringResource(R.string.name), style = body1, color = Grey08)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.name_guide_text),
                textAlign = TextAlign.End,
                style = body3, color = Grey06
            )
        }

        RunCombiTextField(
            value = uiState.name,
            onValueChange = { profileViewModel.onNameChange(it) },
            placeholder = stringResource(R.string.name_hint),
            isError = uiState.isError,
            visualTransformation = VisualTransformation.None,
            enabled = true,
            singleLine = true,
        )

        Spacer(modifier = Modifier.weight(1f))

        RunCombiButton(
            onClick = {
                keyboardController?.hide()
                localFocusManager.clearFocus()

                profileViewModel.validateAndProceed {
                    signupViewModel.setProfile(ProfileData(nickname = uiState.name))
                    onNext()
                }
            },
            text = stringResource(R.string.next),
            enabled = uiState.isButtonEnabled,
        )
    }

    /// 바로 앨범 선택하는 것으로 기획 수정됨
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            containerColor = Grey02
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                RunCombiButton(
                    onClick = {
                        showBottomSheet = false
                        profileViewModel.openAlbum()
                    },
                    text = stringResource(R.string.select_from_album),
                )
                Spacer(modifier = Modifier.height(8.dp))
                RunCombiButton(
                    onClick = {
                        showBottomSheet = false
                        profileViewModel.onSelectCamera()
                    },
                    text = stringResource(R.string.take_picture),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen(onNext = {})
}