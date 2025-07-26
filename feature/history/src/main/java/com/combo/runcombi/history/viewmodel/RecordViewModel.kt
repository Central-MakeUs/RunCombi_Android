package com.combo.runcombi.history.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.ExerciseRating
import com.combo.runcombi.history.model.PetCalUi
import com.combo.runcombi.history.model.RecordUiState
import com.combo.runcombi.history.usecase.GetRunDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(private val getRunDataUseCase: GetRunDataUseCase) :
    ViewModel() {
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun onBack() {}
    fun onEdit() {}
    fun onAddPhoto() {}
    fun onDelete() {}
    fun onRatingSelected(rating: ExerciseRating) {
        _uiState.value = _uiState.value.copy(selectedRating = rating)
    }

    fun onMemoChanged(memo: String) {
        _uiState.value = _uiState.value.copy(memo = memo)
    }

    fun fetchRecord(runId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getRunDataUseCase(runId).collect { result ->
                _uiState.update { it.copy(isLoading = false) }

                when (result) {
                    is DomainResult.Success -> {
                        val runData = result.data
                        _uiState.value = RecordUiState(
                            runTime = runData.runTime,
                            runDistance = runData.runDistance,
                            memberCal = 0,
                            memberImageUrl = "",
                            petCalList = runData.petData.map {
                                PetCalUi(
                                    it.petCal,
                                    it.petImageUrl
                                )
                            },
                            imagePaths = listOfNotNull(
                                runData.runImageUrl,
                                runData.routeImageUrl
                            ).filter { it.isNotBlank() },
                            selectedRating = runData.runEvaluating,
                            memo = runData.memo,
                            isLoading = false
                        )
                    }

                    is DomainResult.Error -> {
                        Log.e("RecordViewModel", "기록 불러오기 실패: $result")
                        _errorMessage.emit(result.message ?: "알 수 없는 에러가 발생했습니다.")
                    }
                    is DomainResult.Exception -> {
                        Log.e("RecordViewModel", "기록 불러오기 실패: $result")
                        _errorMessage.emit(result.error.message ?: "네트워크 에러가 발생했습니다.")
                    }
                }
            }
        }
    }
} 