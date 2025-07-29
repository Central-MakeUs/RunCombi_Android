package com.combo.runcombi.history.screen

import android.widget.Toast
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.history.model.ExerciseRecord
import com.combo.runcombi.history.model.HistoryEvent
import com.combo.runcombi.history.viewmodel.HistoryViewModel
import com.combo.runcombi.ui.ext.clickableSingle
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private fun String.toKoreanStyle(): String = when (this) {
    "WALKING" -> "걷기"
    "JOGGING" -> "조깅"
    "RUNNING" -> "달리기"
    else -> this
}

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onRecordClick: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val showBottomSheet = remember { mutableStateOf(false) }

    // 최초 진입 및 월 변경 시 월 데이터 조회
    LaunchedEffect(uiState.currentYearMonth) {
        viewModel.getMonthData(uiState.currentYearMonth.year, uiState.currentYearMonth.monthValue)
    }

    LaunchedEffect(Unit) {
        viewModel.errorMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF181818))
    ) {
        HistoryContent(
            uiState = uiState,
            showBottomSheet = showBottomSheet.value,
            onPrevMonth = {
                val prev = uiState.currentYearMonth.minusMonths(1)
                viewModel.getMonthData(prev.year, prev.monthValue)
                viewModel.onEvent(HistoryEvent.PrevMonth)
            },
            onNextMonth = {
                val next = uiState.currentYearMonth.plusMonths(1)
                viewModel.getMonthData(next.year, next.monthValue)
                viewModel.onEvent(HistoryEvent.NextMonth)
            },
            onDateSelected = { date ->
                viewModel.getDayData(date.year, date.monthValue, date.dayOfMonth)
                viewModel.onEvent(HistoryEvent.SelectDate(date))
                showBottomSheet.value = true
            },
            onDismissBottomSheet = { showBottomSheet.value = false },
            onRecordClick = {
                showBottomSheet.value = false
                onRecordClick(it)
            }
        )
    }
}

@Composable
fun HistoryContent(
    uiState: com.combo.runcombi.history.model.HistoryUiState,
    showBottomSheet: Boolean = false,
    onPrevMonth: () -> Unit = {},
    onNextMonth: () -> Unit = {},
    onDateSelected: (LocalDate) -> Unit = {},
    onDismissBottomSheet: () -> Unit = {},
    onRecordClick: (Int) -> Unit = {},
) {
    Column {
        HistoryStatsHeader(
            avgTime = uiState.avgTime.toString(),
            avgDistance = String.format("%.2f", uiState.avgDistance),
            mostRunStyle = uiState.mostRunStyle.toKoreanStyle()
        )
        CustomCalendar(
            yearMonth = uiState.currentYearMonth,
            exerciseDayMap = uiState.exerciseDayMap,
            exerciseCount = uiState.exerciseCount,
            onPrevMonth = onPrevMonth,
            onNextMonth = onNextMonth,
            onDateSelected = onDateSelected
        )
    }
    if (showBottomSheet && uiState.selectedDate != null) {
        ExerciseRecordBottomSheet(
            date = uiState.selectedDate,
            records = uiState.exerciseRecords,
            onDismiss = onDismissBottomSheet,
            onRecordClick = onRecordClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryContentPreview() {
    val fakeUiState = com.combo.runcombi.history.model.HistoryUiState(
        currentYearMonth = YearMonth.of(2025, 7),
        exerciseDayMap = mapOf(
            LocalDate.of(2025, 7, 1) to true,
            LocalDate.of(2025, 7, 2) to false,
            LocalDate.of(2025, 7, 3) to true
        ),
        exerciseCount = 2,
        avgTime = 40,
        avgDistance = 5.5,
        mostRunStyle = "JOGGING",
        selectedDate = LocalDate.of(2025, 7, 1),
        exerciseRecords = listOf(
            ExerciseRecord(
                id = 1,
                time = "12:05",
                duration = 40,
                distance = 5.5,
                imageUrl = null
            ),
            ExerciseRecord(
                id = 2,
                time = "13:10",
                duration = 30,
                distance = 3.2,
                imageUrl = null
            )
        )
    )
    HistoryContent(
        uiState = fakeUiState
    )
}

@Composable
fun HistoryStatsHeader(
    avgTime: String,
    avgDistance: String,
    mostRunStyle: String,
) {
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
                Text(avgTime, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("min", color = Color.Gray, fontSize = 12.sp)
            }
            Column {
                Text("평균 운동 거리", color = Color.Gray, fontSize = 12.sp)
                Text(
                    avgDistance,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("km", color = Color.Gray, fontSize = 12.sp)
            }
            Column {
                Text("자주한 운동", color = Color.Gray, fontSize = 12.sp)
                Text(
                    mostRunStyle,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CustomCalendar(
    yearMonth: YearMonth,
    exerciseDayMap: Map<LocalDate, Boolean>,
    exerciseCount: Int,
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
            // 운동한 날 수 표시
            Text(
                "\uD83D\uDC3E $exerciseCount 번",
                color = Color(0xFFB6FF4A),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
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
                            Box(
                                Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        } else {
                            val date = yearMonth.atDay(day)
                            val isExerciseDay = exerciseDayMap[date] == true
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clickable { onDateSelected(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isExerciseDay) {
                                    Icon(
                                        Icons.Default.AccountBox,
                                        contentDescription = "운동함",
                                        tint = Color(0xFFB6FF4A),
                                        modifier = Modifier.size(24.dp)
                                    )
                                } else {
                                    Text(
                                        text = day.toString(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Normal
                                    )
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
    onRecordClick: (Int) -> Unit = {},
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
                        .padding(vertical = 8.dp)
                        .clickableSingle {
                            onRecordClick(record.id)
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Text("🕒 ${record.time}", fontWeight = FontWeight.Bold)
                        Text("운동 시간: ${record.duration} min")
                        Text("운동 거리: ${String.format("%.1f", record.distance)} km")
                    }
                    if (record.imageUrl != null) {
                        Box(
                            Modifier
                                .size(60.dp)
                                .background(Color.Gray, shape = CircleShape)
                        )
                    }
                }
            }
        }
    }
}