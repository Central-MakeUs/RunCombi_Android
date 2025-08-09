package com.combo.runcombi.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.HistoryUiState
import com.combo.runcombi.history.model.MemoEvent
import com.combo.runcombi.history.model.MemoUiState
import com.combo.runcombi.history.model.RecordEvent
import com.combo.runcombi.history.usecase.SetRunMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MemoViewModel @Inject constructor(
    private val setRunMemoUseCase: SetRunMemoUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MemoUiState())
    val uiState: StateFlow<MemoUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<MemoEvent>()
    val eventFlow: SharedFlow<MemoEvent> = _eventFlow.asSharedFlow()

    fun changeMemo(memo: String) {
        _uiState.update { it.copy(memo = memo) }
    }

    fun storeMemo(runId: Int, memo: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            setRunMemoUseCase(
                runId = runId,
                memo = memo
            ).collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _eventFlow.emit(MemoEvent.MemoSuccess)
                    }

                    else -> {
                        _eventFlow.emit(MemoEvent.Error("메모 저장에 실패 했습니다."))
                    }
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }

}