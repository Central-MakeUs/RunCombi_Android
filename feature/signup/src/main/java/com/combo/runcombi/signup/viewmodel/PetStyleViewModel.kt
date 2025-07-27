package com.combo.runcombi.signup.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.domain.user.usecase.SetUserInfoUseCase
import com.combo.runcombi.signup.model.PetStyleUiState
import com.combo.runcombi.signup.model.SignupData
import com.combo.runcombi.signup.model.SignupEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PetStyleViewModel @Inject constructor(
    val setUserInfoUseCase: SetUserInfoUseCase,
    val getUserInfoUseCase: GetUserInfoUseCase,
) :
    ViewModel() {
    private val _uiState = MutableStateFlow(PetStyleUiState())
    val uiState: StateFlow<PetStyleUiState> = _uiState

    private val _eventFlow = MutableSharedFlow<SignupEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun selectStyle(style: RunStyle) {
        _uiState.update {
            it.copy(
                selectedStyle = style,
                isButtonEnabled = true
            )
        }
    }

    fun checkUserStatus() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getUserInfoUseCase().collectLatest { result ->
                _uiState.update { it.copy(isLoading = false) }
                when (result) {
                    is DomainResult.Success -> {
                        val userInfo = result.data
                        val userStatus = userInfo.memberStatus

                        if (userStatus == MemberStatus.LIVE) {
                            _eventFlow.emit(SignupEvent.NavigateNext)
                        } else {
                            _eventFlow.emit(SignupEvent.Error(errorMessage = "잠시 후 다시 시도해 주세요."))
                        }
                    }

                    is DomainResult.Error -> {
                        Log.e(
                            "[PetStyleViewModel]",
                            "[Error]: message: ${result.message}, code: ${result.code}"
                        )
                        _eventFlow.emit(
                            SignupEvent.Error(
                                errorMessage = "네트워크 에러가 발생했습니다."
                            )
                        )
                    }

                    is DomainResult.Exception -> {
                        Log.e("[PetStyleViewModel]", "[Exception]: message: ${result.error}")
                        _eventFlow.emit(
                            SignupEvent.Error(
                                errorMessage = "네트워크 에러가 발생했습니다."
                            )
                        )
                    }
                }
            }
        }
    }

    fun signup(signupData: SignupData) {
        viewModelScope.launch {
            _uiState.update { _uiState.value.copy(isLoading = true) }


            val memeber = with(signupData) {
                Member(
                    nickname = profileData.nickname,
                    gender = genderData.gender,
                    height = bodyData.height ?: 0,
                    weight = bodyData.weight ?: 0,
                )
            }

            val pet = with(signupData) {
                Pet(
                    name = petProfileData.name,
                    age = petInfoData.petAge ?: 0,
                    weight = petInfoData.petWeight?.toDouble() ?: 0.0,
                    runStyle = petStyleData.walkStyle,
                )
            }

            setUserInfoUseCase(
                memberDetail = memeber,
                petDetail = pet,
                memberImage = signupData.profileData.profileFile,
                petImage = signupData.petProfileData.profileFile
            ).collectLatest { result ->
                _uiState.update { _uiState.value.copy(isLoading = false) }

                when (result) {
                    is DomainResult.Success -> {
                        _eventFlow.emit(SignupEvent.Success)
                    }

                    is DomainResult.Error -> {
                        Log.e(
                            "[PetStyleViewModel]",
                            "[Error] message: ${result.message}, code: ${result.code}"
                        )
                        _eventFlow.emit(SignupEvent.Error("네트워크 에러가 발생했습니다."))
                    }

                    is DomainResult.Exception -> {
                        Log.e(
                            "[PetStyleViewModel]",
                            "[Exception] message: ${result.error}"
                        )
                        _eventFlow.emit(SignupEvent.Error("예외가 발생했습니다."))
                    }
                }
            }
        }
    }
}