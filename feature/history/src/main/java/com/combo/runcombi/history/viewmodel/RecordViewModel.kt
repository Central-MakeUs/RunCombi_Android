package com.combo.runcombi.history.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.ExerciseRating
import com.combo.runcombi.history.model.PetCalUi
import com.combo.runcombi.history.model.RecordEvent
import com.combo.runcombi.history.model.RecordUiState
import com.combo.runcombi.history.usecase.DeleteRunDataUseCase
import com.combo.runcombi.history.usecase.GetRunDataUseCase
import com.combo.runcombi.history.usecase.SetRunEvaluatingUseCase
import com.combo.runcombi.history.usecase.SetRunImageUseCase
import com.combo.runcombi.history.usecase.SetRunMemoUseCase
import com.combo.runcombi.history.usecase.UpdateRunDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getRunDataUseCase: GetRunDataUseCase,
    private val setRunImageUseCase: SetRunImageUseCase,
    private val setRunEvaluatingUseCase: SetRunEvaluatingUseCase,
    private val setRunMemoUseCase: SetRunMemoUseCase,
    private val deleteRunDataUseCase: DeleteRunDataUseCase,
    private val updateRunDataUseCase: UpdateRunDataUseCase,
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<RecordEvent>()
    val eventFlow: SharedFlow<RecordEvent> = _eventFlow.asSharedFlow()

    fun setRunImage(runId: Int, runImage: File) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            setRunImageUseCase(
                runId = runId,
                runImage = runImage,
            ).collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        fetchRecord(runId)
                    }

                    else -> {
                        _eventFlow.emit(RecordEvent.Error("이미지 업로드에 실패 했습니다."))
                    }
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun deleteRunData(runId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            deleteRunDataUseCase(
                runId = runId,
            ).collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _eventFlow.emit(RecordEvent.DeleteSuccess)
                    }

                    else -> {
                        _eventFlow.emit(RecordEvent.Error("기록 삭제를 실패 했습니다."))
                    }
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onRatingSelected(runId: Int, rating: ExerciseRating) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            setRunEvaluatingUseCase(
                runId = runId,
                rating = rating,
            ).collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        fetchRecord(runId)
                    }

                    else -> {
                        _eventFlow.emit(RecordEvent.Error("평가 등록에 실패 했습니다."))
                    }
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
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
                            memberCal = runData.memberCal,
                            nickname = runData.nickname,
                            memberImageUrl = runData.profileImgUrl,
                            date = runData.regDate,
                            petCalList = runData.petData.map {
                                PetCalUi(
                                    petName = it.name,
                                    petCal = it.petCal,
                                    petImageUrl = it.petImageUrl

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
                        _eventFlow.emit(RecordEvent.Error(result.message ?: "알 수 없는 에러가 발생했습니다."))
                    }

                    is DomainResult.Exception -> {
                        Log.e("RecordViewModel", "기록 불러오기 실패: $result")
                        _eventFlow.emit(
                            RecordEvent.Error(
                                result.error.message ?: "네트워크 에러가 발생했습니다."
                            )
                        )
                    }
                }
            }
        }
    }
} 