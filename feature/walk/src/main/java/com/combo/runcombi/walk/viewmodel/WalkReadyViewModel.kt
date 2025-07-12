package com.combo.runcombi.walk.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class WalkReadyViewModel : ViewModel() {
    sealed class Event {
        object RequestLocationPermission : Event()
        object None : Event()
    }

    private val _event = MutableStateFlow<Event>(Event.None)
    val event: StateFlow<Event> = _event

    fun requestLocationPermission() {
        _event.value = Event.RequestLocationPermission
    }

    fun consumeEvent() {
        _event.value = Event.None
    }
} 