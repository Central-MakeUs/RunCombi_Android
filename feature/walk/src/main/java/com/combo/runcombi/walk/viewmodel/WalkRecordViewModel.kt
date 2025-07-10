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

    fun addPathPoint(point: LatLng) {
        _uiModel.update { model ->
            val newPath = model.pathPoints + point
            val distanceToAdd = if (model.pathPoints.isNotEmpty()) {
                calculateDistance(model.pathPoints.last(), point)
            } else 0.0
            val threshold = 12.0 // 최소 이동 거리(m)
            val maxSpeed = 3.0   // 1초에 3m 이상 이동은 무시(비정상)
            val speed = if (model.time > 0) distanceToAdd else 0.0
            val newDistance = if (distanceToAdd >= threshold && speed <= maxSpeed) {
                model.distance + distanceToAdd
            } else {
                model.distance
            }
            model.copy(
                pathPoints = newPath,
                distance = newDistance
            )
        }
    }

    fun updateTime(time: Long) {
        _uiModel.update { it.copy(time = time) }
    }

    fun updateSpeed(speed: Double) {
        _uiModel.update { it.copy(speed = speed) }
    }

    fun updateCalorie(calorie: Double) {
        _uiModel.update { it.copy(calorie = calorie) }
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