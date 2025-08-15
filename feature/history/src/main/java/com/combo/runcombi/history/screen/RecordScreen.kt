package com.combo.runcombi.history.screen

import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.NetworkImage
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.component.RunCombiBottomSheet
import com.combo.runcombi.core.designsystem.component.RunCombiDeleteBottomSheet
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey02
import com.combo.runcombi.core.designsystem.theme.Grey03
import com.combo.runcombi.core.designsystem.theme.Grey04
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey07
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body2
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle4
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle5
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.giantsTitle6
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title4
import com.combo.runcombi.feature.history.R
import com.combo.runcombi.history.model.ExerciseRating
import com.combo.runcombi.history.model.PetCalUi
import com.combo.runcombi.history.model.RecordEvent
import com.combo.runcombi.history.model.RecordUiState
import com.combo.runcombi.history.viewmodel.RecordViewModel
import com.combo.runcombi.ui.ext.clickableSingle
import com.combo.runcombi.ui.util.BitmapUtil
import com.combo.runcombi.ui.util.BitmapUtil.resizeBitmap
import com.combo.runcombi.ui.util.FormatUtils
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RecordScreen(
    runId: Int,
    onBack: () -> Unit,
    onMemo: (Int, String) -> Unit,
    onEditRecord: (Int) -> Unit,
    viewModel: RecordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val albumLauncher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
        uri?.let {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
            val resizedBitmap = resizeBitmap(bitmap, 720, 720)
            val runImageFile = BitmapUtil.bitmapToFile(
                context,
                resizedBitmap,
                "run.jpg",
                quality = 92
            )

            viewModel.setRunImage(runId, runImageFile)

        }
    }

    LaunchedEffect(runId) {
        viewModel.fetchRecord(runId)
    }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is RecordEvent.DeleteSuccess -> {
                    onBack()
                }

                is RecordEvent.Error -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    RecordContent(
        uiState = uiState,
        onBack = onBack,
        onEdit = {
            onEditRecord(runId)
        },
        onAddPhoto = { albumLauncher.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
        onDelete = { viewModel.deleteRunData(runId) },
        onRatingSelected = { rating -> viewModel.onRatingSelected(runId, rating) },
        onMemoChanged = {
            onMemo(runId, uiState.memo)
        },
        onAddMemo = {
            onMemo(runId, uiState.memo)
        })
}

@Composable
fun RecordContent(
    uiState: RecordUiState,
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onAddPhoto: () -> Unit = {},
    onDelete: () -> Unit = {},
    onRatingSelected: (ExerciseRating) -> Unit = {},
    onMemoChanged: () -> Unit = {},
    onAddMemo: () -> Unit = {},
) {
    var showDeleteBottomSheet by remember { mutableStateOf(false) }
    Box {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Grey01)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .height(264.dp)
                        .fillMaxWidth()
                ) {
                    RecordImagePager(imagePaths = uiState.imagePaths, onAddPhoto = onAddPhoto)
                    RecordAppBar(
                        date = uiState.date,
                        hasRunImage = uiState.imagePaths.size == 2,
                        onBack = onBack,
                        onEdit = onEdit,
                        onAddPhoto = onAddPhoto,
                        onDelete = { showDeleteBottomSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .zIndex(1f)
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(40.dp)) }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 20.dp)
                    ) {
                        Text("함께 운동한 시간", style = body2, color = Grey07)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    uiState.runTime.toString(),
                                    style = giantsTitle1,
                                    color = Color.White,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.alignByBaseline()
                                )
                                Spacer(modifier = Modifier.width(1.64.dp))
                                Text(
                                    " min",
                                    style = giantsTitle4,
                                    color = Color.White,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.alignByBaseline()
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    FormatUtils.formatDistance(uiState.runDistance),
                                    style = giantsTitle3,
                                    color = Grey07,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.alignByBaseline()
                                )
                                Spacer(modifier = Modifier.width(1.64.dp))
                                Text(
                                    " km",
                                    style = giantsTitle5,
                                    color = Grey06,
                                    fontStyle = FontStyle.Italic,
                                    modifier = Modifier.alignByBaseline()
                                )
                            }
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
            item {
                CalorieProfileCard(
                    profileUrl = uiState.memberImageUrl,
                    name = uiState.nickname,
                    cal = uiState.memberCal,
                    description = getMemberCalorieDescription(uiState.memberCal),
                    isPet = false
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
            items(uiState.petCalList) { petCalUi ->
                CalorieProfileCard(
                    profileUrl = petCalUi.petImageUrl,
                    name = petCalUi.petName,
                    cal = petCalUi.petCal,
                    description = getPetCalorieDescription(petCalUi.petCal),
                    isPet = true
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item {
                RecordRatingSection(
                    selectedRating = uiState.selectedRating, onRatingSelected = onRatingSelected
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
            item {
                RecordMemoSection(
                    memo = uiState.memo, onMemoChanged = onMemoChanged, onAddMemo = onAddMemo
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary01)
            }
        }
    }

    RunCombiDeleteBottomSheet(
        show = showDeleteBottomSheet,
        onDismiss = { showDeleteBottomSheet = false },
        onAccept = {
            showDeleteBottomSheet = false
            onDelete()
        },
        onCancel = { showDeleteBottomSheet = false },
        title = "운동 기록을 삭제할까요?",
        subtitle = "삭제된 기록은 다시 복구할 수 없어요!",
        acceptButtonText = "삭제",
        cancelButtonText = "아니요",
    )
}

@Composable
fun RecordImagePager(
    imagePaths: List<String>,
    onAddPhoto: (() -> Unit)? = null,
) {
    if (imagePaths.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { imagePaths.size })
        HorizontalPager(
            state = pagerState, modifier = Modifier.fillMaxSize()
        ) { page ->
            val imageUrl = imagePaths[page]
            if (imageUrl.isNotEmpty()) {
                NetworkImage(
                    imageUrl = imageUrl,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        if (imagePaths.size > 1) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Grey03.copy(alpha = 0.8f), RoundedCornerShape(2.dp)
                        )
                        .padding(horizontal = 6.5.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${pagerState.currentPage + 1} / ${imagePaths.size}",
                        color = Grey08,
                        style = body1,
                    )
                }
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Grey01),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(119.dp))
                Text("찍은 운동 사진이 없어요,,,", style = giantsTitle4, color = Grey04)
                Spacer(Modifier.height(52.dp))
                Button(
                    onClick = { onAddPhoto?.invoke() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 13.5.dp, vertical = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Grey03,
                        contentColor = Grey06,
                    )
                ) {
                    StableImage(
                        drawableResId = R.drawable.ic_photo, modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("사진 추가하기", color = Grey06, style = title4)
                }
                Spacer(Modifier.height(20.dp))

            }
        }
    }
}

@Composable
fun RecordAppBar(
    date: String,
    hasRunImage: Boolean,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onAddPhoto: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        StableImage(
            drawableResId = R.drawable.ic_back, modifier = Modifier
                .size(24.dp)
                .clickableSingle {
                    onBack()
                })
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = FormatUtils.formatDateKorean(date),
            color = Color.White,
            style = body2,
        )
        Box {
            StableImage(
                drawableResId = R.drawable.ic_menu,
                modifier = Modifier
                    .size(24.dp)
                    .clickableSingle {
                        menuExpanded = true
                    })
            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(
                    extraSmall = RoundedCornerShape(6.dp)
                )
            ) {
                DropdownMenu(
                    expanded = menuExpanded,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                    onDismissRequest = { menuExpanded = false },
                    shape = RoundedCornerShape(6.dp),
                    containerColor = Grey02,
                ) {
                    val itemPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)

                    DropdownMenuItem(
                        text = {
                            Text(
                                "기록 편집", style = body3, color = Grey08, textAlign = TextAlign.Start
                            )
                        },
                        onClick = {
                            menuExpanded = false
                            onEdit()
                        },
                        leadingIcon = {
                            StableImage(
                                drawableResId = R.drawable.ic_pen,
                                modifier = Modifier.size(16.dp),
                                tint = Grey08
                            )
                        },
                        contentPadding = itemPadding,
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                if (hasRunImage) "사진 변경" else "사진 추가",
                                style = body3,
                                color = Grey08,
                                textAlign = TextAlign.Start
                            )
                        }, onClick = {
                            menuExpanded = false
                            onAddPhoto()
                        }, leadingIcon = {
                            StableImage(
                                drawableResId = R.drawable.ic_photo_menu,
                                modifier = Modifier.size(16.dp),
                            )
                        }, contentPadding = itemPadding
                    )

                    DropdownMenuItem(
                        text = {
                            Text(
                                "기록 삭제",
                                style = body3,
                                color = Color(0xFFB04A4A),
                                textAlign = TextAlign.Start
                            )
                        }, onClick = {
                            menuExpanded = false
                            onDelete()
                        }, leadingIcon = {
                            StableImage(
                                drawableResId = R.drawable.ic_trash,
                                modifier = Modifier.size(16.dp),
                            )
                        }, contentPadding = itemPadding
                    )
                }
            }

        }
    }
}

@Composable
fun CalorieProfileCard(
    profileUrl: String,
    name: String,
    cal: Int,
    description: String,
    isPet: Boolean = false,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = Grey02)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(53.dp)
                        .background(Primary01, RoundedCornerShape(2.dp))
                        .padding(3.dp), contentAlignment = Alignment.Center
                ) {
                    NetworkImage(
                        imageUrl = profileUrl,
                        drawableResId = if (isPet) R.drawable.ic_pet_defalut else R.drawable.person_profile,
                        modifier = Modifier
                            .size(47.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(name, color = Grey08, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(25.dp))
            Column {
                Text(
                    description,
                    color = Color.White,
                    style = giantsTitle4,
                    fontStyle = FontStyle.Italic,
                )
                Spacer(modifier = Modifier.height(6.55.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StableImage(
                        drawableResId = R.drawable.ic_fire,
                        modifier = Modifier
                            .width(12.dp)
                            .height(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "$cal",
                        color = Grey07,
                        style = giantsTitle4,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(3.25.dp))
                    Text(
                        " kcal",
                        color = Grey06,
                        style = giantsTitle6,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.alignByBaseline()
                    )
                }
            }

        }
    }
}

@Composable
fun RecordRatingSection(
    selectedRating: ExerciseRating?,
    onRatingSelected: (ExerciseRating) -> Unit,
) {
    val ratings = listOf(
        ExerciseRating.SO_EASY,
        ExerciseRating.EASY,
        ExerciseRating.NORMAL,
        ExerciseRating.HARD,
        ExerciseRating.VERY_HARD
    )
    val labels = listOf("쏘이지", "이지", "보통", "숨참", "힘듦")
    val petImages = listOf(
        R.drawable.ic_pet1,
        R.drawable.ic_pet2,
        R.drawable.ic_pet3,
        R.drawable.ic_pet4,
        R.drawable.ic_pet5
    )

    val petSelectedImages = listOf(
        R.drawable.ic_selected_pet1,
        R.drawable.ic_selected_pet2,
        R.drawable.ic_selected_pet3,
        R.drawable.ic_selected_pet4,
        R.drawable.ic_selected_pet5
    )
    Text(
        "이번 운동 평가", style = body2, color = Grey07, modifier = Modifier.padding(horizontal = 20.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row(modifier = Modifier.padding(horizontal = 20.dp)) {
        ratings.forEachIndexed { idx, rating ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickableSingle {
                        onRatingSelected(rating)
                    }) {
                StableImage(
                    drawableResId = if (rating == selectedRating) petSelectedImages[idx] else petImages[idx],
                    modifier = Modifier
                        .width(62.dp)
                        .height(37.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    labels[idx], color = Grey07, style = body3
                )
            }
            if (idx != ratings.lastIndex) Spacer(modifier = Modifier.width(2.5.dp))
        }
    }
}

@Composable
fun RecordMemoSection(memo: String, onMemoChanged: () -> Unit, onAddMemo: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "메모", style = body2, color = Grey07,
            )
            Spacer(modifier = Modifier.weight(1f))
            if (memo.isNotEmpty()) StableImage(
                drawableResId = R.drawable.ic_pen, modifier = Modifier
                    .size(24.dp)
                    .clickableSingle {
                        onMemoChanged()
                    })
            else StableImage(
                drawableResId = R.drawable.ic_plus,
                modifier = Modifier
                    .size(24.dp)
                    .clickableSingle {
                        onAddMemo()
                    }

            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            memo, style = body3, color = Grey07
        )
    }

}

fun getMemberCalorieDescription(cal: Int): String {
    return when (cal) {
        in 0..49 -> "조금 움직였어요!"
        in 50..99 -> "막대사탕 하나 태웠어요!"
        in 100..149 -> "소프트콘 하나 태웠어요!"
        in 150..199 -> "핫바 하나 태웠어요!"
        in 200..249 -> "도넛 하나 태웠어요!"
        in 250..299 -> "츄러스 하나 태웠어요!"
        in 300..349 -> "밥 한 공기 태웠어요!"
        in 350..399 -> "감자튀김 한 세트 태웠어요!"
        in 400..449 -> "김밥 한 줄 태웠어요!"
        in 450..499 -> "피자 두 조각 태웠어요!"
        in 500..599 -> "라면 한 봉지 태웠어요!"
        in 600..699 -> "파스타 한 접시 태웠어요!"
        in 700..799 -> "치킨 세 조각 태웠어요!"
        in 800..899 -> "햄버거 한 세트 태웠어요!"
        in 900..999 -> "라면 두 봉지 태웠어요!"
        in 1000..1199 -> "과자 두 봉지 태웠어요!"
        in 1200..1499 -> "케이크 한 판 태웠어요!"
        in 1500..1999 -> "햄버거 두 세트 태웠어요!"
        in 2000..2499 -> "치킨 한 마리 태웠어요!"
        else -> "피자 한 판 태웠어요!"
    }
}

fun getPetCalorieDescription(cal: Int): String {
    return when (cal) {
        in 0..9 -> "조금 움직였어요!"
        in 10..19 -> "사료 10알 태웠어요!"
        in 20..29 -> "사료 15알 태웠어요!"
        in 30..39 -> "사료 20알 태웠어요!"
        in 40..49 -> "사료 25알 태웠어요!"
        in 50..59 -> "사료 30알 태웠어요!"
        in 60..69 -> "사료 35알 태웠어요!"
        in 70..79 -> "사료 40알 태웠어요!"
        in 80..89 -> "사료 50알 태웠어요!"
        in 90..99 -> "사료 55알 태웠어요!"
        in 100..119 -> "사료 60알 태웠어요!"
        in 120..139 -> "사료 70알 태웠어요!"
        in 140..159 -> "사료 80알 태웠어요!"
        in 160..179 -> "사료 100알 태웠어요!"
        in 180..199 -> "사료 110알 태웠어요!"
        in 200..249 -> "사료 130알 태웠어요!"
        in 250..299 -> "사료 160알 태웠어요!"
        in 300..349 -> "사료 180알 태웠어요!"
        in 350..399 -> "사료 200알 태웠어요!"
        in 400..Int.MAX_VALUE -> "사료 한 줌 태웠어요!"
        else -> "사료 한 줌 태웠어요!"
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRecordContent() {
    RecordContent(
        uiState = RecordUiState(
            runTime = 40,
            runDistance = 1.12,
            memberCal = 287,
            date = "2025년 7월 30일",
            memberImageUrl = "",
            petCalList = listOf(
                PetCalUi(petCal = 11, petImageUrl = "")
            ),
            imagePaths = listOf(),
            selectedRating = null,
            memo = "오늘 운동 최고!"
        ),
        onBack = {},
        onEdit = {},
        onAddPhoto = {},
        onDelete = {},
        onRatingSelected = {},
        onMemoChanged = {})
} 