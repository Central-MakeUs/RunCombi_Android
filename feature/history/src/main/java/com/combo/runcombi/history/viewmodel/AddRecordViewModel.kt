package com.combo.runcombi.history.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.history.model.AddRecordEvent
import com.combo.runcombi.history.model.AddRecordUiState
import com.combo.runcombi.history.model.PetUiModel
import com.combo.runcombi.history.usecase.AddRunDataUseCase
import com.combo.runcombi.walk.model.ExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField
import javax.inject.Inject

@HiltViewModel
class AddRecordViewModel @Inject constructor(
    private val addRunDataUseCase: AddRunDataUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddRecordUiState())
    val uiState: StateFlow<AddRecordUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<AddRecordEvent>()
    val eventFlow: SharedFlow<AddRecordEvent> = _eventFlow.asSharedFlow()

    init {
        fetchUserAndPets()
    }

    private fun fetchUserAndPets() {
        viewModelScope.launch {
            getUserInfoUseCase().collectLatest { result ->
                when (result) {
                    is DomainResult.Success -> handleUserInfoSuccess(result.data)
                    else -> {}
                }
            }
        }
    }


    private val serverFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
        .optionalStart()
        .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
        .optionalEnd()
        .toFormatter()

    private val displayFormatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy.MM.dd  HH:mm")

    fun initStartDateTime(startDateTime: String) {
        val dateTime = toDisplayDateTime(startDateTime)
        _uiState.update { it.copy(startDateTime = dateTime) }
    }

    fun updateStartDateTime(startDateTime: String) {
        _uiState.update { it.copy(startDateTime = startDateTime) }
    }

    fun updateTime(time: Int?) {
        _uiState.update { it.copy(time = time) }
    }

    fun updateDistance(distance: Double?) {
        _uiState.update { it.copy(distance = distance) }
    }

    fun updateExerciseType(type: ExerciseType) {
        _uiState.update { it.copy(exerciseType = type) }
    }

    fun togglePetSelect(pet: Pet) {
        _uiState.update { state ->
            val petUiList = state.petList
            val tapped = petUiList.find { it.pet == pet } ?: return@update state
            val selectedList = getSelectedPetList(petUiList)

            if (tapped.isSelected) {
                return@update state.copy(petList = deselectPet(petUiList, pet))
            }

            if (selectedList.size == 2) return@update state

            return@update state.copy(petList = selectPet(petUiList, pet, selectedList.size))
        }
    }

    private fun getSelectedPetList(petUiList: List<PetUiModel>): List<PetUiModel> =
        petUiList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }

    private fun deselectPet(petUiList: List<PetUiModel>, pet: Pet): List<PetUiModel> {
        val newList = petUiList.map {
            if (it.pet == pet) it.copy(isSelected = false, selectedOrder = null)
            else it
        }
        val remainSelected =
            newList.filter { it.isSelected }.sortedBy { it.selectedOrder ?: Int.MAX_VALUE }
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

    private fun handleUserInfoSuccess(userInfo: com.combo.runcombi.domain.user.model.UserInfo) {
        val member = userInfo.member
        val petList = userInfo.petList
        _uiState.update {
            it.copy(
                member = member,
                petList = petList.mapIndexed { idx, pet ->
                    PetUiModel(
                        pet,
                        false,
                        idx
                    )
                }
            )
        }
    }


    fun saveRunData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            with(uiState.value) {
                val selectedPets = petList.filter { it.isSelected }
                if (selectedPets.isEmpty()) {
                    _eventFlow.emit(AddRecordEvent.Error(errorMessage = "반려동물을 최소 1마리 선택해 주세요."))
                    _uiState.update { it.copy(isLoading = false) }
                    return@launch
                }

                if (distance == null || distance <= 0.0) {
                    _eventFlow.emit(AddRecordEvent.Error(errorMessage = "거리를 입력해 주세요."))
                    _uiState.update { it.copy(isLoading = false) }
                    return@launch
                }

                if (time == null || time <= 0) {
                    _eventFlow.emit(AddRecordEvent.Error(errorMessage = "시간을 입력해 주세요."))
                    _uiState.update { it.copy(isLoading = false) }
                    return@launch
                }

                if (exerciseType == null) {
                    _eventFlow.emit(AddRecordEvent.Error(errorMessage = "운동 스타일을 선택해 주세요."))
                    _uiState.update { it.copy(isLoading = false) }
                    return@launch
                }

                val regDateServer = toServerDateTime(startDateTime)
                addRunDataUseCase(
                    regDate = regDateServer,
                    memberRunStyle = exerciseType.name,
                    runTime = time,
                    runDistance = distance,
                    petCalList = selectedPets.map { it.pet.id },
                ).collect { result ->
                    when (result) {
                        is DomainResult.Success -> {
                            _eventFlow.emit(AddRecordEvent.AddSuccess)
                        }

                        is DomainResult.Error -> {
                            Log.e("addRunError", "${result.message}")
                            _eventFlow.emit(AddRecordEvent.Error(errorMessage = "기록 추가에 실패 했습니다."))
                        }

                        is DomainResult.Exception -> {
                            Log.e("addRunError", "${result.error.message}")
                            _eventFlow.emit(AddRecordEvent.Error(errorMessage = "기록 추가에 실패 했습니다."))
                        }
                    }
                }
            }

            _uiState.update { it.copy(isLoading = false) }
        }
    }


    private fun toDisplayDateTime(serverDateTime: String?): String {
        if (serverDateTime.isNullOrBlank()) return ""
        return try {
            LocalDateTime.parse(serverDateTime, serverFormatter).format(displayFormatter)
        } catch (e: Exception) {
            serverDateTime
        }
    }

    private fun toServerDateTime(displayDateTime: String?): String {
        if (displayDateTime.isNullOrBlank()) return ""
        return try {
            LocalDateTime.parse(displayDateTime, displayFormatter).format(serverFormatter)
        } catch (e: Exception) {
            displayDateTime
        }
    }
}