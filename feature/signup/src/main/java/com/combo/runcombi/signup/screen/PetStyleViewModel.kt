package com.combo.runcombi.signup.screen

import androidx.lifecycle.ViewModel
import com.combo.runcombi.signup.model.PetStyleType
import com.combo.runcombi.signup.model.PetStyleUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PetStyleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PetStyleUiState())
    val uiState: StateFlow<PetStyleUiState> = _uiState

    fun selectStyle(style: PetStyleType) {
        _uiState.update {
            it.copy(
                selectedStyle = style,
                isButtonEnabled = true
            )
        }
    }

    fun clearStyle() {
        _uiState.value = PetStyleUiState()
    }
} 