package com.combo.runcombi.history.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.EditRecordEvent
import com.combo.runcombi.history.model.EditRecordUiState
import com.combo.runcombi.history.model.MemoEvent
import com.combo.runcombi.history.model.MemoUiState
import com.combo.runcombi.history.model.PetCalUi
import com.combo.runcombi.history.model.RecordEvent
import com.combo.runcombi.history.model.RecordUiState
import com.combo.runcombi.history.usecase.GetRunDataUseCase
import com.combo.runcombi.history.usecase.UpdateRunDataUseCase
import com.combo.runcombi.walk.model.ExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import javax.inject.Inject

@HiltViewModel
class EditRecordViewModel @Inject constructor(
    private val updateRunDataUseCase: UpdateRunDataUseCase,
    private val getRunDataUseCase: GetRunDataUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditRecordUiState())
    val uiState: StateFlow<EditRecordUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<EditRecordEvent>()
    val eventFlow: SharedFlow<EditRecordEvent> = _eventFlow.asSharedFlow()
    
    private val serverFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .optionalEnd()
        .toFormatter()
    private val displayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm")

    fun updateStartDateTime(startDateTime: String) {
        _uiState.update { it.copy(startDateTime = startDateTime) }
    }

    fun updateTime(time: Int?) {
        _uiState.update { it.copy(time = time) }
    }

    fun updateDistance(distance: Double?) {
        _uiState.update { it.copy(distance = distance) }
    }

    fun updateExerciseType(type: ExerciseType) {
        _uiState.update { it.copy(exerciseType = type) }
    }

    fun updateRunData(runId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            with(uiState.value) {
                val regDateServer = toServerDateTime(startDateTime)
                updateRunDataUseCase(
                    runId = runId,
                    regDate = regDateServer,
                    memberRunStyle = (exerciseType ?: ExerciseType.WALKING).name,
                    runTime = time ?: 0,
                    runDistance = distance ?: 0.0,
                ).collect { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            _eventFlow.emit(EditRecordEvent.EditSuccess)
                        }

                        else -> {
                            _eventFlow.emit(EditRecordEvent.Error("기록 편집에 실패 했습니다."))
                        }
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
                        _uiState.value = EditRecordUiState(
                            startDateTime = toDisplayDateTime(runData.regDate),
                            distance = runData.runDistance,
                            exerciseType = ExerciseType.fromOrDefault(runData.memberRunStyle),
                            time = runData.runTime,
                        )
                    }

                    is DomainResult.Error -> {
                        Log.e("RecordViewModel", "기록 불러오기 실패: $result")
                        _eventFlow.emit(
                            EditRecordEvent.Error(
                                result.message ?: "알 수 없는 에러가 발생했습니다."
                            )
                        )
                    }

                    is DomainResult.Exception -> {
                        Log.e("RecordViewModel", "기록 불러오기 실패: $result")
                        _eventFlow.emit(
                            EditRecordEvent.Error(
                                result.error.message ?: "네트워크 에러가 발생했습니다."
                            )
                        )
                    }
                }
            }
        }
    }

    private fun toDisplayDateTime(serverDateTime: String?): String {
        if (serverDateTime.isNullOrBlank()) return ""
        return try {
            LocalDateTime.parse(serverDateTime, serverFormatter).format(displayFormatter)
        } catch (e: Exception) {
            serverDateTime
        }
    }

    private fun toServerDateTime(displayDateTime: String?): String {
        if (displayDateTime.isNullOrBlank()) return ""
        return try {
            LocalDateTime.parse(displayDateTime, displayFormatter).format(serverFormatter)
        } catch (e: Exception) {
            displayDateTime
        }
    }
}