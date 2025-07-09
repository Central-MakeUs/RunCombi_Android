package com.combo.runcombi.walk

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.walk.model.WalkEvent
import com.combo.runcombi.walk.model.WalkUiState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WalkViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(WalkUiState())
    val uiState: StateFlow<WalkUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<WalkEvent>()
    val eventFlow: SharedFlow<WalkEvent> = _eventFlow.asSharedFlow()

    fun requestLocationPermission() {
        emitEvent(WalkEvent.RequestLocationPermission)
    }

    fun updateLocation(latLng: LatLng) {
        _uiState.update { it.copy(myLocation = latLng, isLoading = true) }
        emitEvent(WalkEvent.LocationUpdated(latLng))
    }

    fun updateAddress(address: String) {
        _uiState.update { it.copy(address = address, isLoading = false) }
    }

    fun setError(message: String) {
        _uiState.update { it.copy(isLoading = false) }
        emitEvent(WalkEvent.Error(message))
    }

    private fun emitEvent(event: WalkEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }
} 