package com.combo.runcombi.walk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.walk.model.BottomSheetType
import com.combo.runcombi.walk.model.ExerciseType
import com.combo.runcombi.walk.model.LocationPoint
import com.combo.runcombi.walk.model.WalkMemberUiModel
import com.combo.runcombi.walk.model.WalkPetUIModel
import com.combo.runcombi.walk.model.WalkTrackingEvent
import com.combo.runcombi.walk.model.WalkUiState
import com.combo.runcombi.walk.usecase.CalculateMemberCalorieUseCase
import com.combo.runcombi.walk.usecase.CalculatePetCalorieUseCase
import com.combo.runcombi.walk.usecase.UpdateWalkRecordUseCase
import com.google.android.gms.maps.model.LatLng
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
class WalkRecordViewModel @Inject constructor(
    private val updateWalkRecordUseCase: UpdateWalkRecordUseCase,
    private val calculatePetCalorieUseCase: CalculatePetCalorieUseCase,
    private val calculateMemberCalorieUseCase: CalculateMemberCalorieUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalkUiState())
    val uiState: StateFlow<WalkUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<WalkTrackingEvent>()
    val eventFlow: SharedFlow<WalkTrackingEvent> = _eventFlow.asSharedFlow()

    private var lastPoint: LocationPoint? = null
    private var speedList: List<Double> = emptyList()

    fun addPathPointFromService(
        lat: Double,
        lng: Double,
        accuracy: Float,
        timestamp: Long = System.currentTimeMillis(),
    ) {
        val newPoint = LocationPoint(lat, lng, timestamp, accuracy)
        when (val result = updateWalkRecordUseCase(lastPoint, newPoint, speedList)) {
            is DomainResult.Success -> {
                val data = result.data
                val newDistance = _uiState.value.distance + data.distance
                val newTimeSec = _uiState.value.time
                val newTimeHour = newTimeSec / 3600.0
                val exerciseType = _uiState.value.exerciseType

                val memberUiModel = _uiState.value.walkMemberUiModel
                val memberCalorie = memberUiModel?.let {
                    val member = it.member
                    calculateMemberCalorieUseCase(
                        exerciseType,
                        member.gender.name,
                        member.weight.toDouble(),
                        newTimeHour
                    )
                } ?: 0.0

                val newPetUiModelList = _uiState.value.walkPetUIModelList?.map { petUiModel ->
                    val pet = petUiModel.pet
                    val petCalorie = calculatePetCalorieUseCase(
                        pet.weight,
                        newTimeHour,
                        pet.runStyle.activityFactor
                    )
                    petUiModel.copy(calorie = petCalorie)
                }

                _uiState.update { state ->
                    state.copy(
                        pathPoints = state.pathPoints + LatLng(lat, lng),
                        distance = newDistance,
                        walkMemberUiModel = memberUiModel?.copy(calorie = memberCalorie),
                        walkPetUIModelList = newPetUiModelList
                    )
                }
                lastPoint = newPoint
                speedList =
                    (speedList + (if (lastPoint != null) data.distance / ((newPoint.timestamp - (lastPoint?.timestamp
                        ?: newPoint.timestamp)).coerceAtLeast(1000L) / 1000.0) else 0.0)).takeLast(
                        100
                    )
            }

            is DomainResult.Error, is DomainResult.Exception -> {
                _uiState.update { state ->
                    state.copy(pathPoints = state.pathPoints + LatLng(lat, lng))
                }
                lastPoint = newPoint
            }
        }
    }

    fun updateTime(time: Int) {
        _uiState.update { it.copy(time = time) }
    }

    fun togglePause() {
        _uiState.update { it.copy(isPaused = !it.isPaused) }
    }

    fun clear() {
        _uiState.value = WalkUiState()
        lastPoint = null
        speedList = emptyList()
    }

    fun emitShowBottomSheet(type: BottomSheetType) {
        viewModelScope.launch {
            _eventFlow.emit(WalkTrackingEvent.ShowBottomSheet(type))
        }
    }

    fun initWalkData(exerciseType: ExerciseType, member: Member, petList: List<Pet>) {
        _uiState.update { state ->
            state.copy(
                walkMemberUiModel = WalkMemberUiModel(member),
                walkPetUIModelList = petList.map { WalkPetUIModel(pet = it) },
                exerciseType = exerciseType
            )
        }
    }
}
