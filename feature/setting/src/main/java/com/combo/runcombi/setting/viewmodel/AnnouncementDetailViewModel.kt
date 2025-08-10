package com.combo.runcombi.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.model.AnnouncementDetailEvent
import com.combo.runcombi.setting.model.AnnouncementDetailUiState
import com.combo.runcombi.setting.model.AnnouncementEvent
import com.combo.runcombi.setting.model.AnnouncementUiState
import com.combo.runcombi.setting.usecase.GetAnnouncementDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AnnouncementDetailViewModel @Inject constructor(
    private val getAnnouncementDetailUseCase: GetAnnouncementDetailUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AnnouncementDetailUiState())
    val uiState: StateFlow<AnnouncementDetailUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AnnouncementDetailEvent>()
    val eventFlow: SharedFlow<AnnouncementDetailEvent> = _eventFlow.asSharedFlow()

    private fun emitEvent(event: AnnouncementDetailEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun getAnnouncementDetail(id: Int) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getAnnouncementDetailUseCase(id).collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _uiState.update {
                            it.copy(detail = result.data)
                        }
                    }

                    is DomainResult.Error -> {
                        emitEvent(
                            AnnouncementDetailEvent.Error(
                                result.message ?: "네트워크 에러가 발생했습니다."
                            )
                        )
                    }

                    is DomainResult.Exception -> {
                        emitEvent(
                            AnnouncementDetailEvent.Error(
                                result.error.message ?: "알 수 없는 에러가 발생했습니다."
                            )
                        )
                    }
                }
            }
        }

        _uiState.update { it.copy(isLoading = false) }
    }

    fun openEventApplyUrl(url: String) {
        emitEvent(AnnouncementDetailEvent.OpenEventApplyUrl(url))
    }
}