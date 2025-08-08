package com.combo.runcombi.setting.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiSelectableButton
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title1
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.setting.model.AddPetEvent
import com.combo.runcombi.setting.model.PetStyleData
import com.combo.runcombi.setting.viewmodel.AddPetViewModel
import com.combo.runcombi.setting.viewmodel.PetStyleViewModel
import com.combo.runcombi.ui.ext.clickableWithoutRipple
import com.combo.runcombi.ui.ext.screenDefaultPadding

@Composable
fun AddPetStyleScreen(
    onSuccess: () -> Unit,
    addPetViewModel: AddPetViewModel,
    petStyleViewModel: PetStyleViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    val uiState by petStyleViewModel.uiState.collectAsStateWithLifecycle()
    val eventFlow = petStyleViewModel.eventFlow

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is AddPetEvent.Success -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                    onSuccess()
                }

                is AddPetEvent.Error -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .background(Grey01)
            .clickableWithoutRipple {
                keyboardController?.hide()
                localFocusManager.clearFocus()
            }
    ) {
        Column(
            modifier = Modifier.screenDefaultPadding()
        ) {
            Spacer(Modifier.height(38.dp))
            Text(
                text = "산책스타일을 알려주세요",
                style = title1,
                color = WhiteFF,
                modifier = Modifier.padding(bottom = 9.dp)
            )
            Text(
                text = "더 정확한 반려견 소모 칼로리 계산을 위해 필요해요", style = body1, color = Grey06,
            )
            Spacer(Modifier.height(49.dp))
            Text(
                text = "산책스타일", style = body2, color = Grey08,
            )
            Spacer(Modifier.height(5.dp))
            RunCombiSelectableButton(
                text = "에너지가 넘쳐요!",
                modifier = Modifier.height(40.dp),
                isSelected = uiState.selectedStyle == RunStyle.RUNNING,
                onClick = { petStyleViewModel.selectStyle(RunStyle.RUNNING) },
            )
            Spacer(Modifier.height(14.dp))
            RunCombiSelectableButton(
                text = "여유롭게 걸어요",
                modifier = Modifier.height(40.dp),
                isSelected = uiState.selectedStyle == RunStyle.WALKING,
                onClick = { petStyleViewModel.selectStyle(RunStyle.WALKING) },
            )
            Spacer(Modifier.height(14.dp))
            RunCombiSelectableButton(
                text = "천천히 걸으며 자주 쉬어요",
                modifier = Modifier.height(40.dp),
                isSelected = uiState.selectedStyle == RunStyle.SLOW_WALKING,
                onClick = { petStyleViewModel.selectStyle(RunStyle.SLOW_WALKING) },
            )

            Spacer(Modifier.weight(1f))
            RunCombiButton(
                onClick = {
                    keyboardController?.hide()
                    localFocusManager.clearFocus()

                    addPetViewModel.setPetStyle(PetStyleData(walkStyle = uiState.selectedStyle))

                    val addPetData = addPetViewModel.getAddPetData()
                    petStyleViewModel.addPet(addPetData)
                },
                text = "완료",
                enabled = uiState.isButtonEnabled && !uiState.isLoading
            )
        }
        
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xC2212121)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary01)
            }
        }
    }
}