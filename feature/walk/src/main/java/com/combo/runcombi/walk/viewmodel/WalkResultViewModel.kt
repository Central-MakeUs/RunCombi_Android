package com.combo.runcombi.walk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.walk.model.PermissionType
import com.combo.runcombi.walk.model.WalkResultEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class WalkResultViewModel : ViewModel() {
    private val _eventFlow = MutableSharedFlow<WalkResultEvent>()
    val eventFlow: SharedFlow<WalkResultEvent> = _eventFlow.asSharedFlow()

    private fun emitEvent(event: WalkResultEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun onPermissionDenied(type: PermissionType) = emitEvent(WalkResultEvent.PermissionDenied(type))
    fun onCameraButtonClick() = emitEvent(WalkResultEvent.RequestCameraPermission)
    fun openCamera() = emitEvent(WalkResultEvent.OpenCamera)
}