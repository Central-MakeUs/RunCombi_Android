package com.combo.runcombi.history.model

import java.time.LocalDate
import java.time.YearMonth

// 운동 기록 데이터 모델
data class ExerciseRecord(
    val id: Int,
    val time: String,
    val duration: Int,
    val distance: Double,
    val imageUrl: String?
)

// UI 상태 데이터 클래스
data class HistoryUiState(
    val currentYearMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate? = null,
    val exerciseDays: List<LocalDate> = emptyList(),
    val exerciseRecords: List<ExerciseRecord> = emptyList(),
    val isLoading: Boolean = false,
    val avgTime: Int = 0,
    val avgDistance: Double = 0.0,
    val mostRunStyle: String = "",
    val exerciseDayMap: Map<LocalDate, Boolean> = emptyMap(),
    val exerciseCount: Int = 0,
)

// 이벤트 sealed class
sealed class HistoryEvent {
    object PrevMonth : HistoryEvent()
    object NextMonth : HistoryEvent()
    data class SelectDate(val date: LocalDate) : HistoryEvent()
} 