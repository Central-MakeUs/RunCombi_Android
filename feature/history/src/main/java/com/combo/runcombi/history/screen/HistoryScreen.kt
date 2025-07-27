package com.combo.runcombi.history.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.history.model.ExerciseRecord
import com.combo.runcombi.history.model.HistoryEvent
import com.combo.runcombi.history.viewmodel.HistoryViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF181818))) {
        Column {
            HistoryStatsHeader()
            CustomCalendar(
                yearMonth = uiState.currentYearMonth,
                exerciseDays = uiState.exerciseDays,
                onPrevMonth = { viewModel.onEvent(HistoryEvent.PrevMonth) },
                onNextMonth = { viewModel.onEvent(HistoryEvent.NextMonth) },
                onDateSelected = { date -> viewModel.onEvent(HistoryEvent.SelectDate(date)) }
            )
        }
        if (uiState.isBottomSheetVisible && uiState.selectedDate != null) {
            ExerciseRecordBottomSheet(
                date = uiState.selectedDate ?: LocalDate.now(),
                records = uiState.exerciseRecords,
                onDismiss = { viewModel.onEvent(HistoryEvent.DismissBottomSheet) }
            )
        }
    }
}

@Composable
fun HistoryStatsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF222222))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("이번 달 ", color = Color.White, fontSize = 20.sp)
            Text(
                "우리 콤비는!",
                color = Color(0xFFB6FF4A),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text("평균 운동 시간", color = Color.Gray, fontSize = 12.sp)
                Text("100", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("min", color = Color.Gray, fontSize = 12.sp)
            }
            Column {
                Text("평균 운동 거리", color = Color.Gray, fontSize = 12.sp)
                Text("1.05", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("km", color = Color.Gray, fontSize = 12.sp)
            }
            Column {
                Text("자주한 운동", color = Color.Gray, fontSize = 12.sp)
                Text("조깅", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CustomCalendar(
    yearMonth: YearMonth,
    exerciseDays: List<LocalDate>,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    val daysInMonth = lastDayOfMonth.dayOfMonth
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7 // 일요일=0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF181818))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onPrevMonth) {
                Icon(
                    Icons.Default.KeyboardArrowLeft,
                    contentDescription = "이전달",
                    tint = Color.White
                )
            }
            Text(
                "${yearMonth.year}년 ${
                    yearMonth.month.getDisplayName(
                        TextStyle.SHORT,
                        Locale.KOREAN
                    )
                }",
                color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onNextMonth) {
                Icon(
                    Icons.Default.KeyboardArrowRight,
                    contentDescription = "다음달",
                    tint = Color.White
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        // 요일 헤더
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                Text(it, color = Color.Gray, fontSize = 14.sp, modifier = Modifier.weight(1f))
            }
        }
        Spacer(Modifier.height(4.dp))
        // 날짜 그리드
        val totalCells = startDayOfWeek + daysInMonth
        val rows = (totalCells / 7) + if (totalCells % 7 > 0) 1 else 0
        Column {
            var day = 1
            for (row in 0 until rows) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (col in 0..6) {
                        val cellIndex = row * 7 + col
                        if (cellIndex < startDayOfWeek || day > daysInMonth) {
                            Box(Modifier
                                .weight(1f)
                                .aspectRatio(1f))
                        } else {
                            val date = yearMonth.atDay(day)
                            val isExerciseDay = exerciseDays.contains(date)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clickable { onDateSelected(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = day.toString(),
                                        color = if (isExerciseDay) Color(0xFFB6FF4A) else Color.White,
                                        fontWeight = if (isExerciseDay) FontWeight.Bold else FontWeight.Normal
                                    )
                                    if (isExerciseDay) {
                                        Icon(
                                            Icons.Default.AccountBox,
                                            contentDescription = "운동함",
                                            tint = Color(0xFFB6FF4A),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                            day++
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseRecordBottomSheet(
    date: LocalDate,
    records: List<ExerciseRecord>,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                "${date.year}년 ${date.monthValue}월 ${date.dayOfMonth}일",
                fontWeight = FontWeight.Bold, fontSize = 18.sp
            )
            Spacer(Modifier.height(12.dp))
            records.forEach { record ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("🕒 ${record.time}", fontWeight = FontWeight.Bold)
                        Text("운동 시간: ${record.duration} min")
                        Text("운동 거리: ${record.distance} km")
                    }
                    if (record.imageUrl != null) {
                        Box(
                            Modifier
                                .size(60.dp)
                                .background(Color.Gray, shape = CircleShape)
                        )
                    }
                }
                Divider()
            }
        }
    }
}