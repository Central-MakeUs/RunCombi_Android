package com.combo.runcombi.walk.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.walk.model.WalkUiState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import com.combo.runcombi.walk.model.WalkRecordData
import com.combo.runcombi.walk.model.WalkRecordConstants

class WalkRecordViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WalkUiState())
    val uiState: StateFlow<WalkUiState> = _uiState.asStateFlow()

    private var walkRecordData = WalkRecordData()

    private fun updatePath(point: LatLng, accuracy: Float?, timestamp: Long) {
        if (accuracy == null || accuracy > WalkRecordConstants.MIN_ACCURACY) return
        val prevPoint = walkRecordData.lastValidPoint
        val prevTime = walkRecordData.lastTimestamp
        if (prevPoint != null && prevTime != null) {
            val distance = calculateDistance(prevPoint, point)
            val timeDelta = (timestamp - prevTime).coerceAtLeast(1L) / 1000.0 // 초 단위
            if (distance < WalkRecordConstants.MIN_MOVE_DISTANCE || distance > WalkRecordConstants.MAX_JUMP_DISTANCE) return
            if ((timestamp - prevTime) < WalkRecordConstants.MIN_TIME_INTERVAL || (timestamp - prevTime) > WalkRecordConstants.MAX_TIME_INTERVAL) return
            val speed = distance / timeDelta
            val newSpeedList = walkRecordData.speedList + speed
            _uiState.update { state ->
                val newPath = state.pathPoints + point
                val newDistance = state.distance + distance
                val avgSpeed = if (newSpeedList.isNotEmpty()) newSpeedList.average() else 0.0
                state.copy(
                    pathPoints = newPath,
                    distance = newDistance,
                    speed = avgSpeed,
                    calorie = newDistance * 0.05
                )
            }
            walkRecordData = walkRecordData.copy(
                lastValidPoint = point,
                lastTimestamp = timestamp,
                speedList = newSpeedList
            )
        } else {
            _uiState.update { state ->
                state.copy(pathPoints = state.pathPoints + point)
            }
            walkRecordData = walkRecordData.copy(
                lastValidPoint = point,
                lastTimestamp = timestamp
            )
        }
    }

    fun addPathPointFromService(lat: Double, lng: Double, accuracy: Float, timestamp: Long = System.currentTimeMillis()) =
        updatePath(LatLng(lat, lng), accuracy, timestamp)

    fun updateTime(time: Long) {
        _uiState.update { it.copy(time = time) }
    }

    fun togglePause() {
        _uiState.update { it.copy(isPaused = !it.isPaused) }
    }

    fun clear() {
        _uiState.value = WalkUiState()
        walkRecordData = WalkRecordData()
    }

    private fun calculateDistance(start: LatLng, end: LatLng): Double {
        val radius = 6371000.0 // 지구 반지름(m)
        val dLat = Math.toRadians(end.latitude - start.latitude)
        val dLng = Math.toRadians(end.longitude - start.longitude)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(start.latitude)) * cos(Math.toRadians(end.latitude)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radius * c
    }
}