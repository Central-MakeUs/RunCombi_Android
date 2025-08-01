package com.combo.runcombi.history.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.NetworkImage
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey03
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey07
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle4
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle5
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle6
import com.combo.runcombi.feature.history.R
import com.combo.runcombi.history.model.ExerciseRecord
import com.combo.runcombi.history.model.HistoryEvent
import com.combo.runcombi.history.viewmodel.HistoryViewModel
import com.combo.runcombi.ui.ext.clickableSingle
import com.combo.runcombi.ui.util.FormatUtils
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private fun String.toKoreanStyle(): String = when (this) {
    "SLOW_WALKING" -> "걷기"
    "WALKING" -> "빠르게 걷기"
    "RUNNING" -> "조깅"
    else -> "---"
}

@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onRecordClick: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val showBottomSheet = remember { mutableStateOf(false) }

    LaunchedEffect(uiState.currentYearMonth) {
        viewModel.getMonthData(uiState.currentYearMonth.year, uiState.currentYearMonth.monthValue)
    }

    LaunchedEffect(Unit) {
        viewModel.errorMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    HistoryContent(uiState = uiState, showBottomSheet = showBottomSheet.value, onPrevMonth = {
        val prev = uiState.currentYearMonth.minusMonths(1)
        viewModel.getMonthData(prev.year, prev.monthValue)
        viewModel.onEvent(HistoryEvent.PrevMonth)
    }, onNextMonth = {
        val next = uiState.currentYearMonth.plusMonths(1)
        viewModel.getMonthData(next.year, next.monthValue)
        viewModel.onEvent(HistoryEvent.NextMonth)
    }, onDateSelected = { date ->
        viewModel.getDayData(date.year, date.monthValue, date.dayOfMonth)
        viewModel.onEvent(HistoryEvent.SelectDate(date))
        showBottomSheet.value = true
    }, onDismissBottomSheet = { showBottomSheet.value = false }, onRecordClick = {
        showBottomSheet.value = false
        onRecordClick(it)
    })
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        Column {
            HistoryStatsHeader(
                avgTime = if (uiState.avgTime == 0) "-" else uiState.avgTime.toString(),
                avgDistance = if (uiState.avgDistance.toInt() == 0) "-.--" else FormatUtils.formatDistance(
                    uiState.avgDistance
                ),
                mostRunStyle = uiState.mostRunStyle.toKoreanStyle()
            )
            CustomCalendar(
                yearMonth = uiState.currentYearMonth,
                exerciseDayMap = uiState.exerciseDayMap,
                exerciseCount = uiState.exerciseCount,
                selectedDate = if (showBottomSheet) uiState.selectedDate else null,
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
}

@Composable
fun HistoryStatsHeader(
    avgTime: String,
    avgDistance: String,
    mostRunStyle: String,
) {
    Column(
        modifier = Modifier
            .padding(top = 36.dp, bottom = 32.dp, start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .background(Grey02, RoundedCornerShape(6.dp))
            .padding(vertical = 24.dp, horizontal = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("이번 달 ", color = Color.White, style = giantsTitle3)
            Text(
                "우리 콤비", color = Primary01, style = giantsTitle3
            )
            Text("는!", color = Color.White, style = giantsTitle3)
        }
        Spacer(Modifier.height(13.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "평균 운동 시간",
                    color = Grey06,
                    style = body3,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        avgTime,
                        color = Grey08,
                        style = giantsTitle2,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        " min",
                        color = Grey08,
                        style = giantsTitle6,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "평균 운동 거리",
                    color = Grey06,
                    style = body3,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        avgDistance,
                        color = Grey08,
                        style = giantsTitle2,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.alignByBaseline()
                    )
                    Text(
                        " km",
                        color = Grey08,
                        style = giantsTitle6,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "자주한 운동",
                    color = Grey06,
                    style = body3,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    mostRunStyle,
                    color = Grey08,
                    style = giantsTitle2,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center
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
    selectedDate: LocalDate? = null,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
) {
    val firstDayOfMonth = yearMonth.atDay(1)
    val lastDayOfMonth = yearMonth.atEndOfMonth()
    val daysInMonth = lastDayOfMonth.dayOfMonth
    val startDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    val prevMonth = yearMonth.minusMonths(1)
    val lastDayOfPrevMonth = prevMonth.atEndOfMonth()
    val prevMonthDays = mutableListOf<Int>()
    for (i in startDayOfWeek - 1 downTo 0) {
        prevMonthDays.add(lastDayOfPrevMonth.dayOfMonth - i)
    }

    val nextMonth = yearMonth.plusMonths(1)
    val nextMonthDays = mutableListOf<Int>()
    val totalCells = startDayOfWeek + daysInMonth
    val rows = (totalCells / 7) + if (totalCells % 7 > 0) 1 else 0
    val totalCellsNeeded = rows * 7
    val remainingCells = totalCellsNeeded - totalCells
    for (i in 1..remainingCells) {
        nextMonthDays.add(i)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
        ) {
            StableImage(
                drawableResId = R.drawable.ic_arrow_left,
                modifier = Modifier
                    .size(24.dp)
                    .clickableSingle { onPrevMonth() })
            Spacer(Modifier.width(12.dp))
            Text(
                "${yearMonth.year}년 ${
                    yearMonth.month.getDisplayName(
                        TextStyle.SHORT, Locale.KOREAN
                    )
                }", color = Grey07, style = body2
            )
            Spacer(Modifier.width(12.dp))
            StableImage(
                drawableResId = R.drawable.ic_arrow_right,
                modifier = Modifier
                    .size(24.dp)
                    .clickableSingle { onNextMonth() })
            Spacer(Modifier.weight(1f))
            StableImage(
                drawableResId = R.drawable.ic_paw, modifier = Modifier.size(24.dp)
            )
            Text(
                "$exerciseCount",
                color = Primary01,
                style = giantsTitle4,
                fontStyle = FontStyle.Italic,
            )
            Text(
                " 번",
                color = Primary01,
                style = giantsTitle5,
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(40f / 51f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        it, color = Grey06, style = body2, textAlign = TextAlign.Center
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))

        Column {
            var prevMonthIndex = 0
            var currentDay = 1
            var nextMonthIndex = 0

            for (row in 0 until rows) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    for (col in 0..6) {
                        val cellIndex = row * 7 + col

                        if (cellIndex < startDayOfWeek) {
                            val prevDay = prevMonthDays[prevMonthIndex]
                            val prevDate = prevMonth.atDay(prevDay)
                            val isSelected = selectedDate == prevDate
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(3.dp)
                                    .aspectRatio(40f / 51f)
                                    .then(
                                        if (isSelected) {
                                            Modifier.border(
                                                width = 1.dp,
                                                color = Primary01,
                                            )
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .clickable { onDateSelected(prevDate) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = prevDay.toString(),
                                    color = Grey05.copy(alpha = 0.5f),
                                    style = body3
                                )
                            }
                            prevMonthIndex++
                        } else if (currentDay <= daysInMonth) {
                            val date = yearMonth.atDay(currentDay)
                            val isExerciseDay = exerciseDayMap[date] == true
                            val isSelected = selectedDate == date
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(3.dp)
                                    .aspectRatio(40f / 51f)
                                    .background(color = Grey02)
                                    .padding(2.dp)
                                    .then(
                                        if (isSelected) {
                                            Modifier.border(
                                                width = 2.dp,
                                                color = Primary01,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .clickable { onDateSelected(date) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isExerciseDay) {
                                    StableImage(
                                        drawableResId = R.drawable.ic_paw,
                                        modifier = Modifier.size(24.dp),
                                        tint = if (isSelected) Primary01 else Grey07
                                    )
                                } else {
                                    Text(
                                        text = currentDay.toString(), color = Grey05, style = body3
                                    )
                                }
                            }
                            currentDay++
                        } else {
                            val nextDay = nextMonthDays[nextMonthIndex]
                            val nextDate = nextMonth.atDay(nextDay)
                            val isSelected = selectedDate == nextDate
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(3.dp)
                                    .aspectRatio(40f / 51f)
                                    .then(
                                        if (isSelected) {
                                            Modifier.border(
                                                width = 1.dp,
                                                color = Primary01,
                                            )
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .clickable { onDateSelected(nextDate) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = nextDay.toString(),
                                    color = Grey05.copy(alpha = 0.5f),
                                    style = body3
                                )
                            }
                            nextMonthIndex++
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseRecordContent(
    date: LocalDate,
    records: List<ExerciseRecord>,
    onRecordClick: (Int) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .heightIn(max = 400.dp)
            .padding(top = 16.dp, start = 20.dp, end = 20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "${date.year}년 ${date.monthValue}월 ${date.dayOfMonth}일",
                style = body1,
                color = Color.White
            )
            Spacer(Modifier.weight(1f))
            StableImage(
                drawableResId = R.drawable.ic_plus,
                modifier = Modifier
                    .size(24.dp)
                    .clickableSingle {

                    })
        }
        Spacer(Modifier.height(18.dp))
        LazyColumn {
            items(records) { record ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .background(color = Grey03, RoundedCornerShape(6.dp))
                        .padding(vertical = 10.dp, horizontal = 16.dp)
                        .clickableSingle {
                            onRecordClick(record.id)
                        }, verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            StableImage(
                                drawableResId = R.drawable.ic_clock, modifier = Modifier.size(14.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(record.time, style = body3, color = Grey07)
                        }
                        Spacer(Modifier.height(4.dp))
                        Row {
                            Column {
                                Text("운동 시간", style = body3, color = Grey06)
                                Row {
                                    Text(
                                        "${record.duration}",
                                        style = giantsTitle5,
                                        color = Color.White,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Text(" min", style = body3, color = Grey06)
                                }
                            }
                            Spacer(Modifier.width(32.dp))
                            Column {
                                Text("운동 거리", style = body3, color = Grey06)
                                Row {
                                    Text(
                                        FormatUtils.formatDistance(
                                            distance = record.distance,
                                            decimalPlaces = 1,
                                        ),
                                        style = giantsTitle5,
                                        color = Color.White,
                                        fontStyle = FontStyle.Italic
                                    )
                                    Text(" km", style = body3, color = Grey06)
                                }
                            }

                        }
                    }
                    if (record.imageUrl != null) {
                        NetworkImage(
                            imageUrl = record.imageUrl,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(2.dp)),
                        )
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
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Grey02,
        dragHandle = null,
        shape = RoundedCornerShape(
            topStart = 20.dp, topEnd = 20.dp
        )
    ) {
        ExerciseRecordContent(
            date = date, records = records, onRecordClick = onRecordClick
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
        mostRunStyle = "RUNNING",
        selectedDate = LocalDate.of(2025, 7, 1),
        exerciseRecords = listOf(
            ExerciseRecord(
                id = 1, time = "12:05", duration = 40, distance = 5.5, imageUrl = null
            ), ExerciseRecord(
                id = 2, time = "13:10", duration = 30, distance = 3.2, imageUrl = null
            )
        )
    )
    HistoryContent(
        uiState = fakeUiState
    )
}