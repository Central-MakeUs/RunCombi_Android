package com.combo.runcombi.walk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.analytics.AnalyticsHelper
import com.combo.runcombi.walk.model.BottomSheetType
import com.combo.runcombi.walk.model.ExerciseType
import com.combo.runcombi.walk.model.WalkMemberUiModel
import com.combo.runcombi.walk.model.WalkPetUIModel
import com.combo.runcombi.walk.model.WalkTrackingEvent
import com.combo.runcombi.walk.model.WalkUiState
import com.combo.runcombi.walk.service.WalkTrackingDataManager
import com.combo.runcombi.walk.service.WalkTrackingServiceHelper
import com.combo.runcombi.walk.usecase.CalculateMemberCalorieUseCase
import com.combo.runcombi.walk.usecase.CalculatePetCalorieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class WalkTrackingViewModel @Inject constructor(
    private val calculatePetCalorieUseCase: CalculatePetCalorieUseCase,
    private val calculateMemberCalorieUseCase: CalculateMemberCalorieUseCase,
    private val dataManager: WalkTrackingDataManager,
    private val serviceHelper: WalkTrackingServiceHelper,
    val analyticsHelper: AnalyticsHelper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalkUiState())
    val uiState: StateFlow<WalkUiState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<WalkTrackingEvent>()
    val eventFlow: SharedFlow<WalkTrackingEvent> = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            dataManager.trackingData.collect { serviceData ->
                updateUiStateFromService(serviceData)
            }
        }
    }

    private fun updateUiStateFromService(serviceData: WalkTrackingDataManager.TrackingData) {
        val exerciseType = try {
            ExerciseType.valueOf(serviceData.exerciseType)
        } catch (e: IllegalArgumentException) {
            ExerciseType.WALKING
        }

        val distanceKm = serviceData.distance / 1000.0
        val rounded = BigDecimal(distanceKm).setScale(2, RoundingMode.HALF_UP).toDouble()
        
        val memberUiModel = serviceData.member?.let { member ->
            val memberCalorie = calculateMemberCalorieUseCase(
                exerciseType,
                member.member.gender.name,
                member.member.weight.toDouble(),
                rounded
            ).roundToInt()
            member.copy(calorie = memberCalorie)
        }
        
        val petUiModelList = serviceData.petList?.map { petUiModel ->
            val pet = petUiModel.pet
            val petCalorie = calculatePetCalorieUseCase(
                pet.weight,
                rounded,
                pet.runStyle.activityFactor
            ).roundToInt()
            petUiModel.copy(calorie = petCalorie)
        }

        _uiState.update { state ->
            state.copy(
                time = serviceData.time,
                distance = serviceData.distance,
                pathPoints = serviceData.pathPoints,
                isPaused = serviceData.isPaused,
                exerciseType = exerciseType,
                walkMemberUiModel = memberUiModel,
                walkPetUIModelList = petUiModelList
            )
        }
    }

    fun togglePause() {
        if (_uiState.value.isPaused) {
            serviceHelper.resumeTracking()
        } else {
            serviceHelper.pauseTracking()
        }
    }

    fun emitShowBottomSheet(type: BottomSheetType) {
        viewModelScope.launch {
            _eventFlow.emit(WalkTrackingEvent.ShowBottomSheet(type))
        }
    }

    fun initWalkData(
        exerciseType: ExerciseType,
        member: WalkMemberUiModel,
        petList: List<WalkPetUIModel>,
    ) {
        val initialDistance = 0.0
        val memberCalorie = calculateMemberCalorieUseCase(
            exerciseType,
            member.member.gender.name,
            member.member.weight.toDouble(),
            initialDistance
        ).roundToInt()
        
        val initialPetList = petList.map { petUiModel ->
            val pet = petUiModel.pet
            val petCalorie = calculatePetCalorieUseCase(
                pet.weight,
                initialDistance,
                pet.runStyle.activityFactor
            ).roundToInt()
            petUiModel.copy(calorie = petCalorie)
        }
        
        val initialMember = member.copy(calorie = memberCalorie)

        dataManager.updateInitialData(
            exerciseType.name,
            initialMember,
            initialPetList
        )
        
        // 서비스 시작
        serviceHelper.startTracking(exerciseType)
    }

    fun stopTracking() {
        serviceHelper.stopTracking()
    }

    override fun onCleared() {
        super.onCleared()
        if (serviceHelper.isTracking()) {
            serviceHelper.stopTracking()
        }
    }
}
