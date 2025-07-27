package com.combo.runcombi.history.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.history.model.HistoryEvent
import com.combo.runcombi.history.model.HistoryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth

class HistoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState

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
                _uiState.update { it.copy(selectedDate = event.date, isBottomSheetVisible = true) }
                fetchExerciseRecords(event.date)
            }

            is HistoryEvent.DismissBottomSheet -> {
                _uiState.update { it.copy(isBottomSheetVisible = false) }
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