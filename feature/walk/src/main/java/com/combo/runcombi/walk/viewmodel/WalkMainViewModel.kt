package com.combo.runcombi.walk.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.walk.model.PetUiModel
import com.combo.runcombi.walk.model.WalkEvent
import com.combo.runcombi.walk.model.WalkMainUiState
import com.combo.runcombi.walk.model.WalkData
import com.combo.runcombi.walk.model.ExerciseType
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalkMainViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WalkMainUiState())
    val uiState: StateFlow<WalkMainUiState> = _uiState

    private val _walkData = MutableStateFlow(WalkData())
    val walkData: StateFlow<WalkData> = _walkData

    private val _eventFlow = MutableSharedFlow<WalkEvent>()
    val eventFlow: SharedFlow<WalkEvent> = _eventFlow.asSharedFlow()

    init {
        fetchUserAndPets()
    }

    private fun fetchUserAndPets() {
        viewModelScope.launch {
            getUserInfoUseCase().collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> {
                        val userInfo = result.data
                        val member = userInfo.member
                        val petList = userInfo.petList

                        _walkData.update {
                            it.copy(
                                member = member,
                                petList = petList
                            )
                        }
                        _uiState.update {
                            it.copy(
                                member = member,
                                petUiList = userInfo.petList.mapIndexed { idx, pet ->
                                    PetUiModel(
                                        pet,
                                        false,
                                        idx
                                    )
                                }
                            )
                        }
                    }

                    is DomainResult.Error -> {

                    }

                    is DomainResult.Exception -> {}
                }
            }
        }
    }

    fun togglePetSelect(pet: Pet) {
        _uiState.update { state ->
            val petUiList = state.petUiList
            val tapped = petUiList.find { it.pet == pet } ?: return@update state
            val selectedList =
                petUiList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }

            if (tapped.isSelected) {
                val newList = petUiList.map {
                    if (it.pet == pet) it.copy(isSelected = false, selectedOrder = null)
                    else it
                }
                val remainSelected =
                    newList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }
                val reordered = if (remainSelected.isNotEmpty()) {
                    newList.map {
                        if (it.isSelected) {
                            val idx = remainSelected.indexOfFirst { sel -> sel.pet == it.pet }
                            it.copy(selectedOrder = idx)
                        } else it
                    }
                } else {
                    newList.map { it.copy(selectedOrder = null) }
                }
                return@update state.copy(petUiList = reordered)
            }

            if (selectedList.size == 2) return@update state

            val nextOrder = selectedList.size
            val newList = petUiList.map {
                if (it.pet == pet) it.copy(isSelected = true, selectedOrder = nextOrder)
                else it
            }
            return@update state.copy(petUiList = newList)
        }
    }

    fun updateLocation(latLng: LatLng) {
        _uiState.update { it.copy(myLocation = latLng, isLoading = true) }
    }

    fun updateAddress(address: String) {
        _uiState.update { it.copy(address = address, isLoading = false) }
    }

    fun updateExerciseType(type: ExerciseType) {
        _walkData.update { it.copy(exerciseType = type) }
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