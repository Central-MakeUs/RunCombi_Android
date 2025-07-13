package com.combo.runcombi.walk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.walk.model.BottomSheetType
import com.combo.runcombi.walk.model.LocationPoint
import com.combo.runcombi.walk.model.WalkTrackingEvent
import com.combo.runcombi.walk.model.WalkUiState
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
                val distance = data.distance
                val timeDeltaMs = (newPoint.timestamp - (lastPoint?.timestamp
                    ?: newPoint.timestamp)).coerceAtLeast(1000L)
                val speed = if (lastPoint != null) distance / (timeDeltaMs / 1000.0) else 0.0
                speedList = (speedList + speed).takeLast(100)

                _uiState.update { state ->
                    state.copy(
                        pathPoints = state.pathPoints + LatLng(lat, lng),
                        distance = state.distance + data.distance,
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
}
