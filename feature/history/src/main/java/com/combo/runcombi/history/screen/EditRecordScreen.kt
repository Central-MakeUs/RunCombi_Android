@file:OptIn(ExperimentalMaterial3Api::class)

package com.combo.runcombi.history.screen

import android.widget.NumberPicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title4
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.history.model.EditRecordEvent
import com.combo.runcombi.history.viewmodel.EditRecordViewModel
import com.combo.runcombi.ui.ext.clickableWithoutRipple
import com.combo.runcombi.ui.ext.screenDefaultPadding
import com.combo.runcombi.walk.model.ExerciseType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun EditRecordScreen(
    runId: Int,
    onBack: () -> Unit = {},
    editRecordViewModel: EditRecordViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val uiState by editRecordViewModel.uiState.collectAsStateWithLifecycle()
    val eventFlow = editRecordViewModel.eventFlow

    LaunchedEffect(runId) {
        editRecordViewModel.fetchRecord(runId)
    }

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is EditRecordEvent.EditSuccess -> {
                    onBack()
                }

                is EditRecordEvent.Error -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    var showDateTimeSheet by remember { mutableStateOf(false) }

    EditRecordContent(
        startDateTime = uiState.startDateTime,
        distance = uiState.distance,
        time = uiState.time,
        exerciseType = uiState.exerciseType,
        onCancel = {
            onBack()
        },
        onSave = {
            editRecordViewModel.updateRunData(runId)
        },
        onClickStartDateTime = { showDateTimeSheet = true },
        updateExerciseType = {
            editRecordViewModel.updateExerciseType(it)
        },
        updateDistance = {
            editRecordViewModel.updateDistance(it.toDoubleOrNull() ?: 0.0)
        },
        updateStartDateTime = {
            editRecordViewModel.updateStartDateTime(it)
        },
        updateTime = {
            editRecordViewModel.updateTime(it.toIntOrNull() ?: 0)
        },
    )

    if (showDateTimeSheet) {
        DateTimeBottomSheet(
            initial = uiState.startDateTime,
            onDismiss = { showDateTimeSheet = false },
            onConfirm = { formatted ->
                editRecordViewModel.updateStartDateTime(formatted)
                showDateTimeSheet = false
            }
        )
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Primary01)
        }
    }
}

@Composable
fun EditRecordContent(
    startDateTime: String = "",
    distance: Double = 0.0,
    time: Int = 0,
    exerciseType: ExerciseType? = null,
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
    onClickStartDateTime: () -> Unit = {},
    updateExerciseType: (ExerciseType) -> Unit = {},
    updateDistance: (String) -> Unit = {},
    updateStartDateTime: (String) -> Unit = {},
    updateTime: (String) -> Unit = {},

    ) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        Column {
            EditRecordAppBar(
                onCancel = onCancel,
                onSave = onSave
            )

            Column(
                modifier = Modifier
                    .clickableWithoutRipple {
                        keyboardController?.hide()
                        localFocusManager.clearFocus()
                    }
                    .screenDefaultPadding()
                    .verticalScroll(rememberScrollState()),
            ) {
                Spacer(Modifier.height(30.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickableWithoutRipple { onClickStartDateTime() }
                ) {
                    RunCombiTextField(
                        value = startDateTime,
                        onValueChange = { },
                        placeholder = "",
                        visualTransformation = VisualTransformation.None,
                        enabled = false,
                        singleLine = true,
                        leadingText = "시작 일시",
                        textAlign = TextAlign.End,
                    )
                }

                Spacer(Modifier.height(12.dp))
                Row {
                    RunCombiTextField(
                        modifier = Modifier.weight(1f),
                        value = String.format("%.2f", distance),
                        onValueChange = {
                            updateDistance(it)
                        },
                        placeholder = "",
                        trailingText = "km",
                        visualTransformation = VisualTransformation.None,
                        enabled = true,
                        singleLine = true,
                        leadingText = "거리",
                        textAlign = TextAlign.End,
                    )
                    Spacer(Modifier.width(12.dp))
                    RunCombiTextField(
                        modifier = Modifier.weight(1f),
                        value = time.toString(),
                        onValueChange = {
                            updateTime(it)
                        },
                        placeholder = "",
                        trailingText = "min",
                        visualTransformation = VisualTransformation.None,
                        enabled = true,
                        singleLine = true,
                        leadingText = "시간",
                        textAlign = TextAlign.End,
                    )
                }
                Spacer(Modifier.height(24.dp))
                Text("이번 운동은", color = Color.White, style = title3)
                Spacer(Modifier.height(20.dp))
                RunCombiButton(
                    onClick = {
                        updateExerciseType(ExerciseType.SLOW_WALKING)
                    },
                    textColor = if (exerciseType == ExerciseType.SLOW_WALKING) Grey01 else Grey08,
                    enabledColor = if (exerciseType == ExerciseType.SLOW_WALKING) Primary01 else Grey04,
                    text = "걷기",
                )
                Spacer(Modifier.height(12.dp))
                RunCombiButton(
                    onClick = {
                        updateExerciseType(ExerciseType.WALKING)
                    },
                    textColor = if (exerciseType == ExerciseType.WALKING) Grey01 else Grey08,
                    enabledColor = if (exerciseType == ExerciseType.WALKING) Primary01 else Grey04,
                    text = "빠른 걷기",
                )
                Spacer(Modifier.height(12.dp))
                RunCombiButton(
                    onClick = {
                        updateExerciseType(ExerciseType.RUNNING)
                    },
                    textColor = if (exerciseType == ExerciseType.RUNNING) Grey01 else Grey08,
                    enabledColor = if (exerciseType == ExerciseType.RUNNING) Primary01 else Grey04,
                    text = "조깅",
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DateTimeBottomSheet(
    initial: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm")
    val initialDateTime = try {
        LocalDateTime.parse(initial, formatter)
    } catch (_: Exception) {
        LocalDateTime.now()
    }

    val today = LocalDate.now()
    val dates = (0..13).map { today.plusDays(it.toLong()) }
    val dateLabels = dates.map { date ->
        if (date == today) "오늘" else "${date.monthValue}월 ${date.dayOfMonth}일"
    }

    var dateIndex by remember {
        mutableStateOf(dates.indexOfFirst { it == initialDateTime.toLocalDate() }.coerceAtLeast(0))
    }

    val isPmInit = initialDateTime.hour >= 12
    var ampmIndex by remember { mutableStateOf(if (isPmInit) 1 else 0) } // 0=오전, 1=오후

    val hour12Init = ((initialDateTime.hour % 12).let { if (it == 0) 12 else it })
    var hourIndex by remember { mutableStateOf(hour12Init - 1) } // 0..11

    var minuteIndex by remember { mutableStateOf(initialDateTime.minute) } // 0..59

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Grey02,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF232323))
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text("시작 일시를 선택하세요", color = Color.White, style = title2)
            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AndroidView(
                    modifier = Modifier.weight(1.5f),
                    factory = { context ->
                        NumberPicker(context).apply {
                            minValue = 0
                            maxValue = dateLabels.lastIndex
                            displayedValues = dateLabels.toTypedArray()
                            value = dateIndex
                            wrapSelectorWheel = true
                        }
                    },
                    update = { picker ->
                        picker.displayedValues = dateLabels.toTypedArray()
                        picker.maxValue = dateLabels.lastIndex
                        picker.value = dateIndex
                        picker.setOnValueChangedListener { _, _, newVal -> dateIndex = newVal }
                    }
                )

                AndroidView(
                    modifier = Modifier.weight(1f),
                    factory = { context ->
                        NumberPicker(context).apply {
                            minValue = 0
                            maxValue = 1
                            displayedValues = arrayOf("오전", "오후")
                            value = ampmIndex
                            wrapSelectorWheel = true
                        }
                    },
                    update = { picker ->
                        picker.value = ampmIndex
                        picker.setOnValueChangedListener { _, _, newVal -> ampmIndex = newVal }
                    }
                )

                AndroidView(
                    modifier = Modifier.weight(1f),
                    factory = { context ->
                        NumberPicker(context).apply {
                            minValue = 1
                            maxValue = 12
                            value = hourIndex + 1
                            wrapSelectorWheel = true
                        }
                    },
                    update = { picker ->
                        picker.value = hourIndex + 1
                        picker.setOnValueChangedListener { _, _, newVal -> hourIndex = newVal - 1 }
                    }
                )

                AndroidView(
                    modifier = Modifier.weight(1f),
                    factory = { context ->
                        NumberPicker(context).apply {
                            minValue = 0
                            maxValue = 59
                            value = minuteIndex
                            wrapSelectorWheel = true
                            setFormatter { String.format("%02d", it) }
                        }
                    },
                    update = { picker ->
                        picker.value = minuteIndex
                        picker.setOnValueChangedListener { _, _, newVal -> minuteIndex = newVal }
                    }
                )
            }

            Spacer(Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                RunCombiButton(
                    text = "취소",
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    enabledColor = Grey04,
                    textColor = Grey08,
                )
                Spacer(Modifier.width(10.dp))
                RunCombiButton(
                    text = "선택",
                    onClick = {
                        val baseDate = dates[dateIndex]
                        val hour24 = ((hourIndex + 1) % 12) + if (ampmIndex == 1) 12 else 0
                        val time = LocalTime.of(hour24, minuteIndex)
                        val result = LocalDateTime.of(baseDate, time).format(formatter)
                        onConfirm(result)
                    },
                    modifier = Modifier.weight(1f),
                    enabledColor = Primary01,
                    textColor = Grey01,
                )
            }
        }
    }
}

@Composable
fun EditRecordAppBar(
    onCancel: () -> Unit,
    onSave: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(Grey01)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "취소",
                style = title4,
                color = Grey05,
                modifier = Modifier.clickableWithoutRipple { onCancel() }
            )

            Text(
                text = "기록 편집",
                style = title2,
                color = WhiteFF,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "저장",
                style = title4,
                color = Primary01,
                modifier = Modifier.clickableWithoutRipple { onSave() }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EditRecordContentPreview() {
    EditRecordContent(
        startDateTime = "2025.07.24  20:24",
        distance = 0.65,
        time = 120,
        exerciseType = ExerciseType.WALKING,
    )
}