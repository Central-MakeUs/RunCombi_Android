package com.combo.runcombi.walk.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.usecase.SetRunImageUseCase
import com.combo.runcombi.ui.util.FormatUtils
import com.combo.runcombi.walk.model.WalkData
import com.combo.runcombi.walk.model.WalkResultEvent
import com.combo.runcombi.walk.usecase.EndRunUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class WalkResultViewModel @Inject constructor(
    private val endRunUseCase: EndRunUseCase,
    private val setRunImageUseCase: SetRunImageUseCase,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<WalkResultEvent>()
    val eventFlow: SharedFlow<WalkResultEvent> = _eventFlow.asSharedFlow()

    private fun emitEvent(event: WalkResultEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage: SharedFlow<String> = _errorMessage.asSharedFlow()

    fun saveRun(walkData: WalkData, routeImage: File?) {
        val km = walkData.distance / 1000.0
        val rounded = BigDecimal(km).setScale(2, RoundingMode.HALF_UP).toDouble()

        viewModelScope.launch {
            _isLoading.value = true
            endRunUseCase(
                runId = walkData.runData?.runId ?: 0,
                runTime = walkData.time / 60,
                runDistance = rounded,
                petList = walkData.petList.map {
                    it.pet.id
                },
                routeImage = routeImage,
            ).collect { result ->
                _isLoading.value = false
                when (result) {
                    is DomainResult.Success -> {}
                    is DomainResult.Error -> {
                        Log.e("WalkResultViewModel", "운동 기록 저장 실패: $result")
                        _errorMessage.emit(result.message ?: "알 수 없는 에러가 발생했습니다.")
                        _eventFlow.emit(
                            WalkResultEvent.SaveRunError(
                                message = result.message ?: "네트워크 에러가 발생했습니다."
                            )
                        )
                    }

                    is DomainResult.Exception -> {
                        Log.e("WalkResultViewModel", "운동 기록 저장 실패: $result")
                        _errorMessage.emit(result.error.message ?: "네트워크 에러가 발생했습니다.")
                        _eventFlow.emit(
                            WalkResultEvent.SaveRunError(
                                message = result.error.message ?: "네트워크 에러가 발생했습니다."
                            )
                        )
                    }
                }
            }
        }
    }

    fun setRunImage(runId: Int, runImage: File) {
        viewModelScope.launch {
            _isLoading.value = true
            setRunImageUseCase(
                runId = runId,
                runImage = runImage,
            ).collect { result ->
                _isLoading.value = false
                when (result) {
                    is DomainResult.Success -> {
                        emitEvent(WalkResultEvent.SetRunImageSuccess)
                    }

                    is DomainResult.Error -> {
                        Log.e("WalkResultViewModel", "운동 사진 저장 실패: $result")
                        _errorMessage.emit(result.message ?: "알 수 없는 에러가 발생했습니다.")
                        emitEvent(WalkResultEvent.SetRunImageSuccess)
                    }

                    is DomainResult.Exception -> {
                        Log.e("WalkResultViewModel", "운동 사진 저장 실패: $result")
                        _errorMessage.emit(result.error.message ?: "네트워크 에러가 발생했습니다.")
                        emitEvent(WalkResultEvent.SetRunImageSuccess)
                    }
                }
            }
        }
    }

    fun openCamera() = emitEvent(WalkResultEvent.OpenCamera)
}