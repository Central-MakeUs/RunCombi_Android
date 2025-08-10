package com.combo.runcombi.setting.screen

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.NetworkImage
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar
import com.combo.runcombi.core.designsystem.component.RunCombiButton
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey03
import com.combo.runcombi.core.designsystem.theme.Grey05
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.title1
import com.combo.runcombi.feature.setting.R
import com.combo.runcombi.setting.model.AnnouncementDetailEvent
import com.combo.runcombi.setting.model.AnnouncementDetailUiState
import com.combo.runcombi.setting.viewmodel.AnnouncementDetailViewModel
import com.combo.runcombi.ui.ext.clickableSingle
import com.combo.runcombi.ui.util.FormatUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AnnouncementDetailScreen(
    id: Int,
    onBack: () -> Unit,
    viewModel: AnnouncementDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AnnouncementDetailEvent.Error -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }

                is AnnouncementDetailEvent.OpenEventApplyUrl -> {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(context, "링크를 열 수 없습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    LaunchedEffect(id) {
        viewModel.getAnnouncementDetail(id)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RunCombiAppTopBar(
                onBack = onBack,
                title = "",
                padding = PaddingValues(8.dp)
            )
            AnnouncementDetailContent(
                uiState = uiState,
                onApplyClick = { url -> viewModel.openEventApplyUrl(url) }
            )
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
}

@Composable
fun AnnouncementDetailContent(
    uiState: AnnouncementDetailUiState,
    onApplyClick: (String) -> Unit,
) {
    val detail = uiState.detail ?: return

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            // 상단 고정 영역
            Spacer(modifier = Modifier.height(20.dp))

            TitleSection(
                title = detail.title,
                startDate = detail.startDate,
                endDate = detail.endDate
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 스크롤 가능한 콘텐츠 영역
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                if (detail.content.isNotEmpty()) {
                    ContentSection(content = detail.content)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (detail.announcementImageUrl.isNotEmpty()) {
                    ImageSection(imageUrl = detail.announcementImageUrl)
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (detail.announcementType == "EVENT") {
                if (detail.code.isNotEmpty()) {
                    EventCodeSection(eventCode = detail.code)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (detail.eventApplyUrl.isNotEmpty()) {
                    RunCombiButton(
                        text = "응모하기",
                        onClick = { onApplyClick(detail.eventApplyUrl) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

@Composable
fun TitleSection(
    title: String,
    startDate: String,
    endDate: String,
) {
    Column {
        Text(
            text = title,
            style = title1,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "기간: ${FormatUtils.formatDate(startDate)} ~ ${FormatUtils.formatDate(endDate)}",
            style = body3,
            color = Grey06
        )
    }
}

@Composable
fun ContentSection(
    content: String,
) {
    Text(
        text = content,
        style = body3,
        color = Grey08,
    )
}

@Composable
fun ImageSection(
    imageUrl: String,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
            .background(Grey03.copy(alpha = 0.1f))
    ) {
        NetworkImage(
            imageUrl = imageUrl,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
fun EventCodeSection(
    eventCode: String,
) {
    var isCopied by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column {
        Text(
            text = "이벤트 코드",
            style = body3,
            color = Grey06
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(Grey03, RoundedCornerShape(6.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = eventCode,
                style = body1,
                color = Grey05,
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickableSingle {
                    val clipboardManager =
                        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clipData = ClipData.newPlainText("이벤트 코드", eventCode)
                    clipboardManager.setPrimaryClip(clipData)

                    isCopied = true
                }
            ) {
                StableImage(
                    drawableResId = if (isCopied) R.drawable.ic_copy_complete else R.drawable.ic_copy,
                    modifier = Modifier.size(24.dp),
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = if (isCopied) "복사완료" else "복사하기",
                    style = body3.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF398AEC)
                )
            }
        }

        LaunchedEffect(isCopied) {
            if (isCopied) {
                delay(2000)
                isCopied = false
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF171717)
@Composable
fun PreviewAnnouncementDetailContent() {
    val mockDetail = com.combo.runcombi.setting.model.AnnouncementDetail(
        announcementId = 1,
        title = "앱 v 2.0.0 출시!",
        content = "새로운 기능들이 추가되었습니다. 더 나은 사용자 경험을 제공하기 위해 노력하고 있습니다.",
        announcementType = "EVENT",
        startDate = "2025.08.05",
        endDate = "2025.08.17",
        code = "IDK23W",
        announcementImageUrl = "https://example.com/image.jpg",
        regDate = "2024.03.05",
        eventApplyUrl = "https://example.com/apply"
    )

    val mockUiState = AnnouncementDetailUiState(
        detail = mockDetail,
        isLoading = false
    )

    AnnouncementDetailContent(
        uiState = mockUiState,
        onApplyClick = { url -> }
    )
}