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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.NetworkImage
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.RunCombiDeleteBottomSheet
import com.combo.runcombi.core.designsystem.component.RunCombiTextField
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle4
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title4
import com.combo.runcombi.core.designsystem.theme.WhiteFF
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.feature.history.R
import com.combo.runcombi.history.model.AddRecordEvent
import com.combo.runcombi.history.model.PetUiModel
import com.combo.runcombi.history.viewmodel.AddRecordViewModel
import com.combo.runcombi.ui.ext.clickableWithoutRipple
import com.combo.runcombi.ui.ext.customPolygonClip
import com.combo.runcombi.ui.ext.screenDefaultPadding
import com.combo.runcombi.ui.util.FormatUtils
import com.combo.runcombi.walk.model.ExerciseType
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AddRecordScreen(
    date: String,
    onBack: () -> Unit = {},
    addRecordViewModel: AddRecordViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val uiState by addRecordViewModel.uiState.collectAsStateWithLifecycle()
    val eventFlow = addRecordViewModel.eventFlow

    LaunchedEffect(Unit) {
        addRecordViewModel.initStartDateTime(date)
    }

    LaunchedEffect(Unit) {
        eventFlow.collect { event ->
            when (event) {
                is AddRecordEvent.AddSuccess -> {
                    onBack()
                }

                is AddRecordEvent.Error -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    var showDateTimeSheet by remember { mutableStateOf(false) }
    var showCancelBottomSheet by remember { mutableStateOf(false) }

    AddRecordContent(
        member = uiState.member,
        petUiList = uiState.petList,
        startDateTime = uiState.startDateTime,
        distance = uiState.distance,
        time = uiState.time,
        exerciseType = uiState.exerciseType,
        onCancel = {
            showCancelBottomSheet = true
        },
        onSave = {
            addRecordViewModel.saveRunData()
        },
        onClickStartDateTime = { showDateTimeSheet = true },
        updateExerciseType = {
            addRecordViewModel.updateExerciseType(it)
        },
        updateDistance = {
            val parsed = it.replace(",", ".").toDoubleOrNull()
            addRecordViewModel.updateDistance(parsed)
        },
        updateStartDateTime = {
            addRecordViewModel.updateStartDateTime(it)
        },
        updateTime = {
            val parsed = it.toIntOrNull()
            addRecordViewModel.updateTime(parsed)
        },
        onPetClick = {
            addRecordViewModel.togglePetSelect(it)
        },
    )


    RunCombiDeleteBottomSheet(
        show = showCancelBottomSheet,
        onDismiss = { showCancelBottomSheet = false },
        onAccept = {
            showCancelBottomSheet = false
            onBack()
        },
        onCancel = { showCancelBottomSheet = false },
        title = "기록 작성을 그만두시겠어요?",
        subtitle = "지금까지 입력한 내용은 저장되지 않아요.",
        acceptButtonText = "취소",
        cancelButtonText = "아니요"
    )

    if (showDateTimeSheet) {
        DateTimeBottomSheet(
            initial = uiState.startDateTime,
            onDismiss = { showDateTimeSheet = false },
            onConfirm = { formatted ->
                addRecordViewModel.updateStartDateTime(formatted)
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
fun AddRecordContent(
    startDateTime: String = "",
    member: Member? = null,
    petUiList: List<PetUiModel>,
    distance: Double? = null,
    time: Int? = null,
    exerciseType: ExerciseType? = null,
    onCancel: () -> Unit = {},
    onSave: () -> Unit = {},
    onClickStartDateTime: () -> Unit = {},
    updateExerciseType: (ExerciseType) -> Unit = {},
    updateDistance: (String) -> Unit = {},
    updateStartDateTime: (String) -> Unit = {},
    updateTime: (String) -> Unit = {}, onPetClick: (Pet) -> Unit = {},

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
            AddRecordAppBar(
                onCancel = onCancel,
                onSave = onSave,
                isSaveEnabled = petUiList.any { it.isSelected } && distance != null && time != null && exerciseType != null
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
                    var distanceText by remember {
                        mutableStateOf(distance?.let {
                            FormatUtils.formatDistance(it)
                        } ?: "")
                    }
                    var timeText by remember { mutableStateOf(time?.toString() ?: "") }
                    var isDistanceFocused by remember { mutableStateOf(false) }
                    var isTimeFocused by remember { mutableStateOf(false) }

                    LaunchedEffect(distance, isDistanceFocused) {
                        if (!isDistanceFocused) {
                            distanceText = distance?.let { FormatUtils.formatDistance(it) } ?: ""
                        }
                    }
                    LaunchedEffect(time, isTimeFocused) {
                        if (!isTimeFocused) {
                            timeText = time?.toString() ?: ""
                        }
                    }

                    RunCombiTextField(
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { focusState ->
                                isDistanceFocused = focusState.isFocused
                                if (!focusState.isFocused) {
                                    if (distanceText.isBlank()) {
                                        updateDistance("")
                                    } else {
                                        val parsed = distanceText.replace(",", ".").toDoubleOrNull()
                                        distanceText =
                                            parsed?.let { FormatUtils.formatDistance(it) } ?: ""
                                        updateDistance(distanceText)
                                    }
                                }
                            },
                        value = distanceText,
                        onValueChange = { newValue ->
                            distanceText = newValue
                            updateDistance(newValue)
                        },
                        placeholder = "",
                        trailingText = "km",
                        visualTransformation = VisualTransformation.None,
                        enabled = true,
                        singleLine = true,
                        leadingText = "거리",
                        textAlign = TextAlign.End,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Next
                        ),
                    )
                    Spacer(Modifier.width(12.dp))
                    RunCombiTextField(
                        modifier = Modifier
                            .weight(1f)
                            .onFocusChanged { focusState ->
                                isTimeFocused = focusState.isFocused
                                if (!focusState.isFocused) {
                                    if (timeText.isBlank()) {
                                        updateTime("")
                                    } else {
                                        val parsed = timeText.toIntOrNull()
                                        timeText = parsed?.toString() ?: ""
                                        updateTime(timeText)
                                    }
                                }
                            },
                        value = timeText,
                        onValueChange = { newValue ->
                            timeText = newValue
                            updateTime(newValue)
                        },
                        placeholder = "",
                        trailingText = "min",
                        visualTransformation = VisualTransformation.None,
                        enabled = true,
                        singleLine = true,
                        leadingText = "시간",
                        textAlign = TextAlign.End,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                    )
                }
                Spacer(Modifier.height(24.dp))
                Text("함께한 콤비는", color = Color.White, style = title3)
                Spacer(Modifier.height(20.dp))
                CombiList(
                    member = member,
                    petUiList = petUiList,
                    onPetClick = onPetClick
                )
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

@ExperimentalMaterial3Api
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
        mutableIntStateOf(dates.indexOfFirst { it == initialDateTime.toLocalDate() }
            .coerceAtLeast(0))
    }

    val isPmInit = initialDateTime.hour >= 12
    var ampmIndex by remember { mutableIntStateOf(if (isPmInit) 1 else 0) } // 0=오전, 1=오후

    val hour12Init = ((initialDateTime.hour % 12).let { if (it == 0) 12 else it })
    var hourIndex by remember { mutableIntStateOf(hour12Init - 1) } // 0..11

    var minuteIndex by remember { mutableIntStateOf(initialDateTime.minute) } // 0..59

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
fun AddRecordAppBar(
    onCancel: () -> Unit,
    onSave: () -> Unit,
    isSaveEnabled: Boolean = true,
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
                text = "기록 추가",
                style = title2,
                color = WhiteFF,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "저장",
                style = title4,
                color = if (isSaveEnabled) Primary01 else Grey05,
                modifier = if (isSaveEnabled) {
                    Modifier.clickableWithoutRipple { onSave() }
                } else {
                    Modifier
                }
            )
        }
    }
}

@Composable
private fun MemberProfile(member: Member) {
    Box(
        modifier = Modifier
            .height(77.dp)
            .width(84.dp), contentAlignment = Alignment.Center
    ) {
        StableImage(
            drawableResId = R.drawable.ic_user_box,
            modifier = Modifier
                .height(77.dp)
                .width(84.dp),
        )

        NetworkImage(
            contentScale = ContentScale.Crop,
            imageUrl = member.profileImageUrl,
            drawableResId = R.drawable.person_profile,
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .padding(end = 12.dp)
                .customPolygonClip(
                    bottomLeft = true,
                    topRight = true,
                    polygonSize = 16.dp,
                    bottomLeftAngle = 62.0
                )
        )
    }
}

@Composable
private fun PetProfile(
    pet: Pet,
    isSelected: Boolean,
    isCenter: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val boxImage = when {
            isCenter && isSelected -> R.drawable.ic_pet_box_center_selected
            isCenter && !isSelected -> R.drawable.ic_pet_box_center
            isSelected -> R.drawable.ic_pet_box_selected
            else -> R.drawable.ic_pet_box
        }

        val boxHeight = if (isCenter) 77.dp else 77.dp
        val boxWidth = if (isCenter) 99.dp else 85.dp
        val startPadding = 12.dp
        val endPadding = if (isCenter) 12.dp else 0.dp

        Box(
            modifier = Modifier
                .height(boxHeight)
                .width(boxWidth)
                .clickableWithoutRipple { onClick?.invoke() },
            contentAlignment = Alignment.Center
        ) {
            StableImage(
                drawableResId = boxImage,
                modifier = Modifier
                    .height(boxHeight)
                    .width(boxWidth),
            )
            NetworkImage(
                contentScale = ContentScale.Crop,
                imageUrl = pet.profileImageUrl,
                drawableResId = R.drawable.ic_pet_defalut,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = startPadding, end = endPadding)
                    .padding(6.dp)
                    .customPolygonClip(
                        bottomLeft = true,
                        topRight = true,
                        polygonSize = 16.dp,
                        bottomLeftAngle = 58.0,
                        topRightAngle = 63.0
                    )
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            pet.name,
            style = body1,
            color = Grey06,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = startPadding),
        )
    }
}

@Composable
private fun CombiList(
    member: Member?,
    petUiList: List<PetUiModel>,
    onPetClick: (Pet) -> Unit,
) {
    val selected = petUiList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }
    val unselected = petUiList.filter { !it.isSelected }.sortedBy { it.originIndex }
    val allPets = selected + unselected

    Column(
        modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("함께 운동할 콤비", style = giantsTitle4, color = Grey08)
        Spacer(modifier = Modifier.height(25.dp))
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            member?.let {
                MemberProfile(member = it)
            }
            allPets.forEachIndexed { index, petUi ->
                PetProfile(
                    pet = petUi.pet,
                    isSelected = petUi.isSelected,
                    isCenter = allPets.size > 1 && index == 0,
                    onClick = { onPetClick(petUi.pet) }
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        if (selected.isNotEmpty()) {
            val selectedCombis = selected.map { it.pet.name }.joinToString()
            Text(
                "${selectedCombis}와 함께!",
                style = body1,
                color = Primary01,
                textAlign = TextAlign.Center
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