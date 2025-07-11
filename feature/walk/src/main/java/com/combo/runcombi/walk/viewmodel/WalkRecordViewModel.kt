package com.combo.runcombi.walk.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.walk.model.WalkUiModel
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class WalkRecordViewModel : ViewModel() {
    private val _uiModel = MutableStateFlow(WalkUiModel())
    val uiModel: StateFlow<WalkUiModel> = _uiModel

    fun addPathPoint(point: LatLng, timeDeltaSec: Long = 1) {
        _uiModel.update { model ->
            val newPath = model.pathPoints + point
            if (model.pathPoints.isEmpty()) {
                // 첫 위치: 거리 누적 없이 pathPoints만 추가
                model.copy(pathPoints = newPath)
            } else {
                val distanceToAdd = calculateDistance(model.pathPoints.last(), point)
                val newDistance = model.distance + distanceToAdd
                val avgSpeed = if (model.time + timeDeltaSec > 0) newDistance / (model.time + timeDeltaSec) else 0.0
                model.copy(
                    pathPoints = newPath,
                    distance = newDistance,
                    speed = avgSpeed,
                    calorie = newDistance * 0.05
                )
            }
        }
    }

    fun updateTime(time: Long) {
        _uiModel.update { it.copy(time = time) }
    }

    fun clear() {
        _uiModel.value = WalkUiModel()
    }

    fun calculateDistance(start: LatLng, end: LatLng): Double {
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