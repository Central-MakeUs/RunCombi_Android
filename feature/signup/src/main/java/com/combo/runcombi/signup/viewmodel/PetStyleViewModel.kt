package com.combo.runcombi.signup.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.pet.model.WalkStyle
import com.combo.runcombi.signup.model.PetStyleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PetStyleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PetStyleUiState())
    val uiState: StateFlow<PetStyleUiState> = _uiState

    fun selectStyle(style: WalkStyle) {
        _uiState.update {
            it.copy(
                selectedStyle = style,
                isButtonEnabled = true
            )
        }
    }
}