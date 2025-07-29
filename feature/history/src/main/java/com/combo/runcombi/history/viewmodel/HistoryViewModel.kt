package com.combo.runcombi.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.HistoryEvent
import com.combo.runcombi.history.model.HistoryUiState
import com.combo.runcombi.history.usecase.GetDayDataUseCase
import com.combo.runcombi.history.usecase.GetMonthDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import java.time.LocalDateTime
import com.combo.runcombi.history.model.ExerciseRecord
import java.time.ZoneId
import java.time.ZonedDateTime

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getMonthDataUseCase: GetMonthDataUseCase,
    private val getDayDataUseCase: GetDayDataUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun onEvent(event: HistoryEvent) {
        when (event) {
            is HistoryEvent.PrevMonth -> {
                val prev = _uiState.value.currentYearMonth.minusMonths(1)
                _uiState.update { it.copy(currentYearMonth = prev) }
                fetchExerciseDays(prev)
            }

            is HistoryEvent.NextMonth -> {
                val next = _uiState.value.currentYearMonth.plusMonths(1)
                _uiState.update { it.copy(currentYearMonth = next) }
                fetchExerciseDays(next)
            }

            is HistoryEvent.SelectDate -> {
                _uiState.update { it.copy(selectedDate = event.date) }
                fetchExerciseRecords(event.date)
            }
        }
    }

    fun getMonthData(year: Int, month: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getMonthDataUseCase(year, month).collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        val data = result.data
                        val exerciseDayMap = data.monthData.associate { runDate ->
                            val localDate = LocalDate.parse(runDate.date, DateTimeFormatter.ofPattern("yyyyMMdd"))
                            localDate to (runDate.runId != null && runDate.runId.isNotEmpty())
                        }
                        val exerciseDays = exerciseDayMap.filterValues { it }.keys.toList()
                        val exerciseCount = exerciseDays.size
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                avgTime = data.avgTime,
                                avgDistance = data.avgDistance,
                                mostRunStyle = data.mostRunStyle,
                                exerciseDays = exerciseDays,
                                exerciseDayMap = exerciseDayMap,
                                exerciseCount = exerciseCount
                            )
                        }
                    }
                    is DomainResult.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        viewModelScope.launch { _errorMessage.emit("월 데이터 조회 실패") }
                    }
                    is DomainResult.Exception -> {
                        _uiState.update { it.copy(isLoading = false) }
                        viewModelScope.launch { _errorMessage.emit("예기치 못한 오류 발생") }
                    }
                }
            }
        }
    }

    fun getDayData(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getDayDataUseCase(year, month, day).collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        val records = result.data.map { item ->
                            ExerciseRecord(
                                id = item.runId,
                                time = try {
                                    val utcDateTime = LocalDateTime.parse(item.regDate)
                                    val kstDateTime = utcDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                                    "%02d:%02d".format(kstDateTime.hour, kstDateTime.minute)
                                } catch (e: Exception) {
                                    ""
                                },
                                duration = item.runTime,
                                distance = item.runDistance,
                                imageUrl = item.runImageUrl
                            )
                        }
                        _uiState.update { state ->
                            state.copy(
                                isLoading = false,
                                exerciseRecords = records
                            )
                        }
                    }
                    is DomainResult.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        viewModelScope.launch { _errorMessage.emit("일별 데이터 조회 실패") }
                    }
                    is DomainResult.Exception -> {
                        _uiState.update { it.copy(isLoading = false) }
                        viewModelScope.launch { _errorMessage.emit("예기치 못한 오류 발생") }
                    }
                }
            }
        }
    }

    private fun fetchExerciseDays(yearMonth: YearMonth) {
        // TODO: API 연동 후 exerciseDays 업데이트
        // 예시: _uiState.update { it.copy(exerciseDays = listOf(LocalDate.of(2025, 7, 1), ...)) }
    }

    private fun fetchExerciseRecords(date: LocalDate) {
        // TODO: API 연동 후 exerciseRecords 업데이트
        // 예시: _uiState.update { it.copy(exerciseRecords = listOf(...)) }
    }
} 