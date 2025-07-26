package com.combo.runcombi.history.screen

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.history.model.ExerciseRating
import com.combo.runcombi.history.model.PetCalUi
import com.combo.runcombi.history.model.RecordUiState
import com.combo.runcombi.history.viewmodel.RecordViewModel
import com.combo.runcombi.ui.util.FormatUtils
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.platform.LocalContext

@Composable
fun RecordScreen(
    runId: Int,
    viewModel: RecordViewModel = hiltViewModel(),
) {
    LaunchedEffect(runId) {
        viewModel.fetchRecord(runId)
    }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.errorMessage.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    RecordContent(
        uiState = uiState,
        onBack = { viewModel.onBack() },
        onEdit = { viewModel.onEdit() },
        onAddPhoto = { viewModel.onAddPhoto() },
        onDelete = { viewModel.onDelete() },
        onRatingSelected = { rating -> viewModel.onRatingSelected(rating) },
        onMemoChanged = { memo -> viewModel.onMemoChanged(memo) }
    )
}

@Composable
fun RecordContent(
    uiState: RecordUiState,
    onBack: () -> Unit = {},
    onEdit: () -> Unit = {},
    onAddPhoto: () -> Unit = {},
    onDelete: () -> Unit = {},
    onRatingSelected: (ExerciseRating) -> Unit = {},
    onMemoChanged: (String) -> Unit = {},
) {
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth()
            ) {
                RecordImagePager(imagePaths = uiState.imagePaths, onAddPhoto = onAddPhoto)
                RecordAppBar(
                    date = "",
                    onBack = onBack,
                    onEdit = onEdit,
                    onAddPhoto = onAddPhoto,
                    onDelete = onDelete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .zIndex(1f)
                )
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("총 운동한 시간", fontSize = 16.sp, color = Color.LightGray)
                    Text(
                        FormatUtils.formatTime(uiState.runTime),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("이동 거리", fontSize = 16.sp, color = Color.LightGray)
                    Text(
                        FormatUtils.formatDistance(uiState.runDistance),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item {
            CalorieProfileCard(
                profileUrl = uiState.memberImageUrl,
                name = "나(멤버)",
                cal = uiState.memberCal,
                description = getCalorieDescription(uiState.memberCal)
            )
        }
        item { Spacer(modifier = Modifier.height(8.dp)) }
        items(uiState.petCalList) { petCalUi ->
            CalorieProfileCard(
                profileUrl = petCalUi.petImageUrl,
                name = "펫",
                cal = petCalUi.petCal,
                description = getCalorieDescription(petCalUi.petCal)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item {
            RecordRatingSection(
                selectedRating = uiState.selectedRating,
                onRatingSelected = onRatingSelected
            )
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
        item {
            RecordMemoSection(
                memo = uiState.memo,
                onMemoChanged = onMemoChanged
            )
        }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

@Composable
fun RecordImagePager(
    imagePaths: List<String>,
    onAddPhoto: (() -> Unit)? = null,
) {
    if (imagePaths.isNotEmpty()) {
        val pagerState = rememberPagerState(pageCount = { imagePaths.size })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val bitmap = remember(imagePaths[page]) { BitmapFactory.decodeFile(imagePaths[page]) }
            bitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        if (imagePaths.size > 1) {
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/${imagePaths.size}",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Button(onClick = { onAddPhoto?.invoke() }) {
                Text("사진 추가하기", color = Color.White)
            }
        }
    }
}

@Composable
fun RecordAppBar(
    date: String,
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
            .padding(top = 16.dp, start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기", tint = Color.White)
        }
        Text(
            text = date,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Box {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "더보기", tint = Color.White)
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier.background(Color(0xFF232323))
            ) {
                DropdownMenuItem(
                    text = { Text("기록 편집") },
                    onClick = {
                        menuExpanded = false
                        onEdit()
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Edit, contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("사진 추가") },
                    onClick = {
                        menuExpanded = false
                        onAddPhoto()
                    },
                    leadingIcon = {
                        Icon(Icons.Default.AccountBox, contentDescription = null)
                    }
                )
                DropdownMenuItem(
                    text = { Text("기록 삭제", color = Color.Red) },
                    onClick = {
                        menuExpanded = false
                        onDelete()
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                    }
                )
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
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF232323))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지 (비어있으면 아이콘)
            if (profileUrl.isNotEmpty()) {
                // 실제 앱에서는 Coil 등 이미지 라이브러리 사용 권장
                // 예시: AsyncImage(model = profileUrl, contentDescription = null, ...)
                Icon(Icons.Default.AccountBox, contentDescription = null, tint = Color.Yellow) // 임시
            } else {
                Icon(Icons.Default.AccountBox, contentDescription = null, tint = Color.Yellow)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(description, color = Color.White)
            }
            Spacer(modifier = Modifier.weight(1f))
            Text("${cal} kcal", color = Color.White)
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
    Text("운동 평가", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(8.dp))
    Row {
        ratings.forEachIndexed { idx, rating ->
            Button(
                onClick = { onRatingSelected(rating) },
                colors = if (selectedRating == rating) ButtonDefaults.buttonColors(
                    containerColor = Color(
                        0xFF4CAF50
                    )
                ) else ButtonDefaults.buttonColors(),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    labels[idx],
                    color = if (selectedRating == rating) Color.White else Color.Black
                )
            }
            if (idx != ratings.lastIndex) Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun RecordMemoSection(memo: String, onMemoChanged: (String) -> Unit) {
    Text("메모", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = memo,
        onValueChange = onMemoChanged,
        placeholder = { Text("오늘의 운동을 기록해보세요") },
        modifier = Modifier.fillMaxWidth()
    )
}

// 칼로리 값에 따라 문구 반환 함수
fun getCalorieDescription(cal: Int): String {
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

@Preview(showBackground = true)
@Composable
fun PreviewRecordContent() {
    RecordContent(
        uiState = RecordUiState(
            runTime = 40,
            runDistance = 1.12,
            memberCal = 287,
            memberImageUrl = "",
            petCalList = listOf(
                PetCalUi(petCal = 11, petImageUrl = "")
            ),
            imagePaths = listOf(),
            selectedRating = ExerciseRating.NORMAL,
            memo = "오늘 운동 최고!"
        ),
        onBack = {},
        onEdit = {},
        onAddPhoto = {},
        onDelete = {},
        onRatingSelected = {},
        onMemoChanged = {}
    )
} 