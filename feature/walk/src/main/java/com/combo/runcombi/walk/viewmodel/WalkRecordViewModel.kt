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
            val distanceToAdd = if (model.pathPoints.isNotEmpty()) {
                calculateDistance(model.pathPoints.last(), point)
            } else 0.0
            val threshold = 3.0 // 최소 이동 거리(m) - GPS 오차 감안
            val newDistance = if (distanceToAdd >= threshold) {
                model.distance + distanceToAdd
            } else {
                model.distance
            }
            // 순간 속도 계산 (거리/시간)
            val instantSpeed = if (timeDeltaSec > 0) distanceToAdd / timeDeltaSec else 0.0
            // 평균 속도 계산 (누적 거리/누적 시간)
            val avgSpeed = if (model.time + timeDeltaSec > 0) newDistance / (model.time + timeDeltaSec) else 0.0
            model.copy(
                pathPoints = newPath,
                distance = newDistance,
                speed = avgSpeed,
                // 칼로리도 즉시 갱신
                calorie = newDistance * 0.05
            )
        }
    }

    fun updateTime(time: Long) {
        _uiModel.update { it.copy(time = time) }
    }

    fun clear() {
        _uiModel.value = WalkUiModel()
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