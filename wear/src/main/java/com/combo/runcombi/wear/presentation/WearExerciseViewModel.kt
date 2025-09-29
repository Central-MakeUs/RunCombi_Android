package com.combo.runcombi.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.walk.model.ExerciseType
import com.combo.runcombi.walk.usecase.MidRunUpdateUseCase
import com.combo.runcombi.walk.usecase.StartRunUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WearExerciseUiState(
    val isTracking: Boolean = false,
    val isPaused: Boolean = false,
    val startTime: Long = 0L,
    val pausedTime: Long = 0L,
    val totalPausedDuration: Long = 0L,
    val distance: Double = 0.0,
    val currentTime: String = "00:00:00",
    val runId: Int? = null,
    val selectedPetIds: List<Int> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCompleted: Boolean = false,
)

@HiltViewModel
class WearExerciseViewModel @Inject constructor(
    private val startRunUseCase: StartRunUseCase,
    private val midRunUpdateUseCase: MidRunUpdateUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WearExerciseUiState())
    val uiState: StateFlow<WearExerciseUiState> = _uiState.asStateFlow()

    fun startExercise(pets: List<Pet>, exerciseType: ExerciseType) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val petIds = pets.map { it.id }
                _uiState.value = _uiState.value.copy(selectedPetIds = petIds)

                val result = startRunUseCase(petIds, exerciseType.name)

                when (result) {
                    is DomainResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isTracking = true,
                            startTime = System.currentTimeMillis(),
                            runId = result.data.runId
                        )
                    }

                    is DomainResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "운동 시작 실패: ${result.message}"
                        )
                    }

                    else -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "알 수 없는 오류"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "운동 시작 중 오류 발생: ${e.message}"
                )
            }
        }
    }

    fun pauseExercise() {
        _uiState.value = _uiState.value.copy(
            isPaused = true,
            pausedTime = System.currentTimeMillis()
        )
    }

    fun resumeExercise() {
        val currentTime = System.currentTimeMillis()
        val pausedDuration = currentTime - _uiState.value.pausedTime
        _uiState.value = _uiState.value.copy(
            isPaused = false,
            totalPausedDuration = _uiState.value.totalPausedDuration + pausedDuration,
            pausedTime = 0L
        )
    }

    fun finishExercise() {
        viewModelScope.launch {
            val currentState = _uiState.value
            if (currentState.runId == null) return@launch
            
            // 정지 상태가 아니면 먼저 정지
            if (!currentState.isPaused) {
                _uiState.value = currentState.copy(isPaused = true)
            }

            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val currentTime = System.currentTimeMillis()
                val totalElapsed =
                    currentTime - currentState.startTime - currentState.totalPausedDuration
                val runTimeSeconds = (totalElapsed / 1000).toInt()

                // 선택된 펫 ID들 사용
                val petIds = currentState.selectedPetIds

                // 산책 중간 업데이트 API 호출
                val midRunUpdateResult = midRunUpdateUseCase(
                    runId = currentState.runId,
                    runTime = runTimeSeconds,
                    runDistance = currentState.distance
                )

                when (midRunUpdateResult) {
                    is DomainResult.Success -> {
                        // 중간 업데이트 성공 시 운동 완료 처리
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isTracking = false,
                            isPaused = false,
                            isCompleted = true
                        )
                    }
                    is DomainResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "산책 중간 업데이트 실패: ${midRunUpdateResult.message}"
                        )
                    }
                    else -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "산책 중간 업데이트 중 알 수 없는 오류"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "운동 기록 저장 중 오류 발생: ${e.message}"
                )
            }
        }
    }

    fun updateTime(timeString: String) {
        _uiState.value = _uiState.value.copy(currentTime = timeString)
    }

    fun updateDistance(distance: Double) {
        _uiState.value = _uiState.value.copy(distance = distance)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
