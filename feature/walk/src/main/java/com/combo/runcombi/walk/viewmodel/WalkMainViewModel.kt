package com.combo.runcombi.walk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.pet.usecase.GetPetListUseCase
import com.combo.runcombi.walk.model.WalkEvent
import com.combo.runcombi.walk.model.WalkMainUiState
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalkMainViewModel @Inject constructor(
    getUserInfoUseCase: GetUserInfoUseCase,
    getPetListUseCase: GetPetListUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WalkMainUiState())
    val uiState: StateFlow<WalkMainUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<WalkEvent>()
    val eventFlow: SharedFlow<WalkEvent> = _eventFlow.asSharedFlow()

    fun updateLocation(latLng: LatLng) {
        _uiState.update { it.copy(myLocation = latLng, isLoading = true) }
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

    fun onStartWalkClicked(hasLocationPermission: Boolean) {
        if (!hasLocationPermission) {
            emitEvent(WalkEvent.RequestLocationPermission)
        }
    }
}