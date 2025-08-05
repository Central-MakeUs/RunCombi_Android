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
import com.combo.runcombi.walk.model.WalkMemberUiModel
import com.combo.runcombi.walk.model.WalkPetUIModel
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
import com.combo.runcombi.walk.usecase.StartRunUseCase

@HiltViewModel
class WalkMainViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val startRunUseCase: StartRunUseCase,
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

    fun fetchUserAndPets() {
        viewModelScope.launch {
            getUserInfoUseCase().collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> handleUserInfoSuccess(result.data)
                    else -> {}
                }
            }
        }
    }

    fun checkWithInitWalkData(): Boolean {
        val member = _uiState.value.member
        val petList = _uiState.value.petUiList.filter { it.isSelected }
            .map { it -> WalkPetUIModel(pet = it.pet) }

        if (member != null && petList.isNotEmpty()) {
            _walkData.update {
                it.copy(
                    member = WalkMemberUiModel(member = member),
                    petList = petList
                )
            }

            return true
        } else {
            return false
        }
    }

    private fun handleUserInfoSuccess(userInfo: com.combo.runcombi.domain.user.model.UserInfo) {
        val member = userInfo.member
        val petList = userInfo.petList
        _uiState.update {
            it.copy(
                member = member,
                petUiList = petList.mapIndexed { idx, pet ->
                    PetUiModel(
                        pet,
                        false,
                        idx
                    )
                }
            )
        }
    }

    fun togglePetSelect(pet: Pet) {
        _uiState.update { state ->
            val petUiList = state.petUiList
            val tapped = petUiList.find { it.pet == pet } ?: return@update state
            val selectedList = getSelectedPetList(petUiList)

            if (tapped.isSelected) {
                return@update state.copy(petUiList = deselectPet(petUiList, pet))
            }

            if (selectedList.size == 2) return@update state

            return@update state.copy(petUiList = selectPet(petUiList, pet, selectedList.size))
        }
    }

    private fun getSelectedPetList(petUiList: List<PetUiModel>): List<PetUiModel> =
        petUiList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }

    private fun deselectPet(petUiList: List<PetUiModel>, pet: Pet): List<PetUiModel> {
        val newList = petUiList.map {
            if (it.pet == pet) it.copy(isSelected = false, selectedOrder = null)
            else it
        }
        val remainSelected = newList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }
        return if (remainSelected.isNotEmpty()) {
            newList.map {
                if (it.isSelected) {
                    val idx = remainSelected.indexOfFirst { sel -> sel.pet == it.pet }
                    it.copy(selectedOrder = idx)
                } else it
            }
        } else {
            newList.map { it.copy(selectedOrder = null) }
        }
    }

    private fun selectPet(petUiList: List<PetUiModel>, pet: Pet, nextOrder: Int): List<PetUiModel> =
        petUiList.map {
            if (it.pet == pet) it.copy(isSelected = true, selectedOrder = nextOrder)
            else it
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

    private fun emitEvent(event: WalkEvent) {
        viewModelScope.launch { _eventFlow.emit(event) }
    }

    fun onStartWalkClicked(hasLocationPermission: Boolean) {
        if (!hasLocationPermission) {
            emitEvent(WalkEvent.RequestLocationPermission)
        }
    }

    fun setResultData(
        time: Int,
        distance: Double,
        pathPoints: List<LatLng>,
        petList: List<WalkPetUIModel>,
        member: WalkMemberUiModel?,
    ) {
        _walkData.update {
            it.copy(
                time = time,
                distance = distance,
                pathPoints = pathPoints,
                petList = petList,
                member = member
            )
        }
    }

    fun clearResultData() {
        _walkData.update { it.copy(time = 0, distance = 0.0, pathPoints = emptyList()) }
    }

    fun startRun() {
        viewModelScope.launch {
            val member = walkData.value.member
            val petList = walkData.value.petList
            val exerciseType = walkData.value.exerciseType
            if (member != null && petList.isNotEmpty()) {
                val result = startRunUseCase(petList.map { it.pet.id }, exerciseType.name)
                val runData = (result as? DomainResult.Success)?.data
                if (runData != null) {
                    _walkData.update { it.copy(runData = runData) }
                }
            }
        }
    }
}