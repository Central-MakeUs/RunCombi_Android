package com.combo.runcombi.setting.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.component.RunCombiAppTopBar
import com.combo.runcombi.core.designsystem.component.StableImage
import com.combo.runcombi.core.designsystem.theme.Grey01
import com.combo.runcombi.core.designsystem.theme.Grey03
import com.combo.runcombi.core.designsystem.theme.Grey06
import com.combo.runcombi.core.designsystem.theme.Grey07
import com.combo.runcombi.core.designsystem.theme.Grey08
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body1
import com.combo.runcombi.core.designsystem.theme.RunCombiTypography.body3
import com.combo.runcombi.feature.setting.R
import com.combo.runcombi.setting.model.Announcement
import com.combo.runcombi.setting.model.AnnouncementEvent
import com.combo.runcombi.setting.model.AnnouncementUiState
import com.combo.runcombi.setting.viewmodel.AnnouncementViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AnnouncementScreen(
    onBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: AnnouncementViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AnnouncementEvent.Error -> {
                    Toast.makeText(context, event.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            RunCombiAppTopBar(
                onBack = onBack,
                title = "알림",
                padding = PaddingValues(8.dp)
            )
            AnnouncementContent(
                uiState = uiState,
                onNavigateToDetail = onNavigateToDetail
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
fun AnnouncementContent(
    uiState: AnnouncementUiState,
    onNavigateToDetail: (Int) -> Unit,
) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Grey01)
    ) {
        TabRow(
            selectedTab = selectedTab,
            onTabSelected = { selectedTab = it }
        )

        when (selectedTab) {
            0 -> NoticeList(
                noticeList = uiState.noticeList,
                onNavigateToDetail = onNavigateToDetail
            )

            1 -> EventList(
                eventList = uiState.eventList,
                onNavigateToDetail = onNavigateToDetail
            )
        }
    }
}

@Composable
fun TabRow(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
) {
    val tabs = listOf("공지", "이벤트")

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Grey01)
        ) {
            tabs.forEachIndexed { index, title ->
                TabItem(
                    title = title,
                    isSelected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun TabItem(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clickable { onClick() }
            .background(Grey01),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = body1,
                color = if (isSelected) Grey08 else Grey07
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (isSelected) {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(2.dp)
                        .background(Primary01)
                )
            } else {
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(1.dp)
                        .background(Grey03)
                )
            }
        }
    }
}

@Composable
fun NoticeList(
    noticeList: List<Announcement>,
    onNavigateToDetail: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        noticeList.forEach { notice ->
            AnnouncementItem(
                announcement = notice,
                onClick = { onNavigateToDetail(notice.announcementId) }
            )
        }
    }
}

@Composable
fun EventList(
    eventList: List<Announcement>,
    onNavigateToDetail: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        eventList.forEach { event ->
            AnnouncementItem(
                announcement = event,
                onClick = { onNavigateToDetail(event.announcementId) }
            )
        }
    }
}

@Composable
fun AnnouncementItem(
    announcement: com.combo.runcombi.setting.model.Announcement,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clickable { onClick() }
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = announcement.title,
                style = body1,
                color = Grey08
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = announcement.regDate,
                style = body3,
                color = Grey06
            )
        }

        StableImage(
            drawableResId = R.drawable.ic_arrow_right2,
            modifier = Modifier
                .size(18.dp)
                .padding(start = 8.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Grey03)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewAnnouncementContent() {
    val mockUiState = AnnouncementUiState(
        eventList = listOf(
            com.combo.runcombi.setting.model.Announcement(
                announcementId = 1,
                title = "앱 v 2.0.0 출시!",
                announcementType = "EVENT",
                regDate = "2024.03.05",
                startDate = "2024.03.05",
                endDate = "2024.04.05",
                isRead = "N"
            )
        ),
        noticeList = listOf(
            com.combo.runcombi.setting.model.Announcement(
                announcementId = 2,
                title = "개인정보 처리방침 변경",
                announcementType = "NOTICE",
                regDate = "2024.03.05",
                startDate = "2024.03.05",
                endDate = "2024.04.05",
                isRead = "N"
            )
        ),
        isLoading = false
    )

    AnnouncementContent(
        uiState = mockUiState,
        onNavigateToDetail = {}
    )
}