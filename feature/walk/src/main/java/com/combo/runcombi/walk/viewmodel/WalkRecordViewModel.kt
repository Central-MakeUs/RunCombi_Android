package com.combo.runcombi.walk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.walk.model.LocationPoint
import com.combo.runcombi.walk.model.WalkUiState
import com.combo.runcombi.walk.usecase.UpdateWalkRecordUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WalkRecordViewModel @Inject constructor(
    private val updateWalkRecordUseCase: UpdateWalkRecordUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WalkUiState())
    val uiState: StateFlow<WalkUiState> = _uiState.asStateFlow()

    private var lastPoint: LocationPoint? = null

    fun addPathPointFromService(
        lat: Double,
        lng: Double,
        accuracy: Float,
        timestamp: Long = System.currentTimeMillis(),
    ) {
        val newPoint = LocationPoint(lat, lng, timestamp, accuracy)

        viewModelScope.launch {
            when (val result = updateWalkRecordUseCase.execute(lastPoint, newPoint)) {
                is DomainResult.Success -> {
                    val data = result.data
                    _uiState.update { state ->
                        state.copy(
                            pathPoints = state.pathPoints + LatLng(lat, lng),
                            distance = state.distance + data.distance,
                            speed = data.averageSpeed,
                            calorie = data.calorie
                        )
                    }
                    lastPoint = newPoint
                }

                is DomainResult.Error, is DomainResult.Exception -> {
                    _uiState.update { state ->
                        state.copy(pathPoints = state.pathPoints + LatLng(lat, lng))
                    }
                    lastPoint = newPoint
                }
            }
        }
    }

    fun updateTime(time: Long) {
        _uiState.update { it.copy(time = time) }
    }

    fun togglePause() {
        _uiState.update { it.copy(isPaused = !it.isPaused) }
    }

    fun clear() {
        _uiState.value = WalkUiState()
        lastPoint = null
        updateWalkRecordUseCase.reset()
    }
}
