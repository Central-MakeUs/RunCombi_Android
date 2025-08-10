package com.combo.runcombi.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.model.SuggestionEvent
import com.combo.runcombi.setting.model.SuggestionUiState
import com.combo.runcombi.setting.usecase.SuggestionUseCase
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
class SuggestionViewModel @Inject constructor(
    private val suggestionUseCase: SuggestionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SuggestionUiState())
    val uiState: StateFlow<SuggestionUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<SuggestionEvent>()
    val eventFlow: SharedFlow<SuggestionEvent> = _eventFlow.asSharedFlow()

    private fun emitEvent(event: SuggestionEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun changeSuggestion(suggestion: String) {
        _uiState.update { it.copy(suggestion = suggestion) }
    }

    fun suggest() {
        val message = _uiState.value.suggestion

        if (message.isEmpty()) {
            emitEvent(SuggestionEvent.Error(message = "의견을 작성해주세요"))
            return
        }

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            suggestionUseCase(message).collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        emitEvent(SuggestionEvent.Success)
                    }

                    else -> {
                        emitEvent(SuggestionEvent.Error("알 수 없는 오류가 발생했습니다."))
                    }

                }
            }
        }
        _uiState.update { it.copy(isLoading = false) }
    }
}