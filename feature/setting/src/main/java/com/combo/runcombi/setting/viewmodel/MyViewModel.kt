package com.combo.runcombi.setting.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.model.MyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState())
    val uiState: StateFlow<MyUiState> = _uiState

    init {
        fetchUserInfo()
    }

    fun refreshUserInfo() {
        fetchUserInfo()
    }

    private fun fetchUserInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getUserInfoUseCase().collect { result ->
                when (result) {
                    is DomainResult.Success -> {
                        _uiState.update {
                            it.copy(
                                member = result.data.member,
                                petList = result.data.petList,
                                isLoading = false
                            )
                        }
                    }
                    else -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }
} 