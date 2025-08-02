package com.combo.runcombi.setting.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.domain.user.usecase.AddPetUseCase
import com.combo.runcombi.setting.model.AddPetData
import com.combo.runcombi.setting.model.AddPetEvent
import com.combo.runcombi.setting.model.PetStyleUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PetStyleViewModel @Inject constructor(
    private val addPetUseCase: AddPetUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PetStyleUiState())
    val uiState: StateFlow<PetStyleUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<AddPetEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun selectStyle(style: RunStyle) {
        _uiState.update {
            it.copy(
                selectedStyle = style,
                isButtonEnabled = true
            )
        }
    }

    fun addPet(addPetData: AddPetData) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val pet = with(addPetData) {
                Pet(
                    name = profileData.name,
                    age = infoData.petAge ?: 0,
                    weight = infoData.petWeight ?: 0.0,
                    runStyle = styleData.walkStyle,
                )
            }

            addPetUseCase(
                petDetail = pet,
                petImage = addPetData.profileData.profileFile
            ).collect { result ->
                _uiState.update { it.copy(isLoading = false) }

                when (result) {
                    is DomainResult.Success -> {
                        _eventFlow.emit(AddPetEvent.Success("반려견이 성공적으로 추가되었습니다."))
                    }

                    is DomainResult.Error -> {
                        Log.e(
                            "[PetStyleViewModel]",
                            "[Error] message: ${result.message}, code: ${result.code}"
                        )
                        _eventFlow.emit(AddPetEvent.Error("네트워크 에러가 발생했습니다."))
                    }

                    is DomainResult.Exception -> {
                        Log.e(
                            "[PetStyleViewModel]",
                            "[Exception] message: ${result.error}"
                        )
                        _eventFlow.emit(AddPetEvent.Error("예외가 발생했습니다."))
                    }
                }
            }
        }
    }
} 