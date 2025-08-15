package com.combo.runcombi.walk.service

import android.util.Log
import com.combo.runcombi.walk.model.LocationPoint
import com.combo.runcombi.walk.model.WalkMemberUiModel
import com.combo.runcombi.walk.model.WalkPetUIModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalkTrackingDataManager @Inject constructor() {
    companion object {
        private const val TAG = "WalkTrackingDataManager"
    }
    
    private val _trackingData = MutableStateFlow(TrackingData())
    val trackingData: StateFlow<TrackingData> = _trackingData.asStateFlow()
    
    fun updateLocationData(
        pathPoints: List<LatLng>,
        distance: Double,
        time: Int
    ) {
        Log.d(TAG, "updateLocationData: 경로점=${pathPoints.size}, 거리=${distance}m, 시간=${time}초")
        _trackingData.update { it.copy(
            pathPoints = pathPoints,
            distance = distance,
            time = time
        ) }
    }
    
    fun updateMemberData(member: WalkMemberUiModel?) {
        Log.d(TAG, "updateMemberData: 멤버 업데이트 - $member")
        _trackingData.update { it.copy(member = member) }
    }
    
    fun updatePetListData(petList: List<WalkPetUIModel>?) {
        Log.d(TAG, "updatePetListData: 반려동물 리스트 업데이트 - $petList")
        _trackingData.update { it.copy(petList = petList) }
    }
    
    fun updateExerciseType(exerciseType: String) {
        Log.d(TAG, "updateExerciseType: 운동 타입 업데이트 - $exerciseType")
        _trackingData.update { it.copy(exerciseType = exerciseType) }
    }
    
    fun updateInitialData(
        exerciseType: String,
        member: WalkMemberUiModel,
        petList: List<WalkPetUIModel>
    ) {
        Log.d(TAG, "updateInitialData: 초기 데이터 설정 - 타입:$exerciseType, 멤버:${member}, 반려동물:${petList}")
        _trackingData.update { it.copy(
            exerciseType = exerciseType,
            member = member,
            petList = petList,
            time = 0,
            distance = 0.0,
            pathPoints = emptyList(),
            isPaused = false,
            isTracking = true
        ) }
    }
    
    fun updatePauseState(isPaused: Boolean) {
        Log.d(TAG, "updatePauseState: 일시정지 상태 업데이트 - $isPaused")
        _trackingData.update { it.copy(isPaused = isPaused) }
    }
    
    fun updateTrackingState(isTracking: Boolean) {
        Log.d(TAG, "updateTrackingState: 추적 상태 업데이트 - $isTracking")
        _trackingData.update { it.copy(isTracking = isTracking) }
    }
    
    fun resetData() {
        Log.d(TAG, "resetData: 추적 데이터 초기화")
        _trackingData.value = TrackingData()
    }
    
    data class TrackingData(
        val exerciseType: String = "",
        val time: Int = 0,
        val distance: Double = 0.0,
        val pathPoints: List<LatLng> = emptyList(),
        val member: WalkMemberUiModel? = null,
        val petList: List<WalkPetUIModel>? = null,
        val isPaused: Boolean = false,
        val isTracking: Boolean = false
    )
}
