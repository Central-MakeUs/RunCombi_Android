package com.combo.runcombi.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.model.AnnouncementEvent
import com.combo.runcombi.setting.model.AnnouncementUiState
import com.combo.runcombi.setting.usecase.GetAnnouncementListUseCase
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
class AnnouncementViewModel @Inject constructor(
    private val getAnnouncementListUseCase: GetAnnouncementListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AnnouncementUiState())
    val uiState: StateFlow<AnnouncementUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<AnnouncementEvent>()
    val eventFlow: SharedFlow<AnnouncementEvent> = _eventFlow.asSharedFlow()

    private fun emitEvent(event: AnnouncementEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    init {
        getAnnouncementList()
    }

    private fun getAnnouncementList() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getAnnouncementListUseCase().collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        val eventList = result.data.filter {
                            it.announcementType == "EVENT"
                        }

                        val noticeList = result.data.filter {
                            it.announcementType == "NOTICE"
                        }
                        _uiState.update {
                            it.copy(eventList = eventList, noticeList = noticeList)
                        }
                    }

                    else -> {}
                }
            }
        }

        _uiState.update { it.copy(isLoading = false) }
    }
}