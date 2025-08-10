package com.combo.runcombi.setting.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.combo.runcombi.core.designsystem.theme.Primary01
import com.combo.runcombi.setting.model.AnnouncementDetailEvent
import com.combo.runcombi.setting.model.AnnouncementEvent
import com.combo.runcombi.setting.viewmodel.AnnouncementDetailViewModel
import com.combo.runcombi.setting.viewmodel.AnnouncementViewModel
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
            }
        }
    }

    LaunchedEffect(id) {
        viewModel.getAnnouncementDetail(id)
    }

    /// TODO: Add AnnouncementDetailContent


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

/// TODO: Add AnnouncementDetailContent

/// TODO: Add AnnouncementDetailContent 프리뷰