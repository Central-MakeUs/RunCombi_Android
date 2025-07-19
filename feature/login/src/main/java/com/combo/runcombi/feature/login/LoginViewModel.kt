package com.combo.runcombi.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.auth.usecase.LoginUseCase
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.usecase.GetUserInfoUseCase
import com.combo.runcombi.feature.login.model.LoginData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun login(loginData: LoginData) {
        viewModelScope.launch {
            when (loginData) {
                is LoginData.Success -> handleLoginSuccess(loginData)
                is LoginData.Failed -> _eventFlow.emit(LoginEvent.Error)
            }
        }
    }

    private suspend fun handleLoginSuccess(loginData: LoginData.Success) {
        try {
            val result = loginUseCase.invoke(token = loginData.token)
            if (result == null) {
                _eventFlow.emit(LoginEvent.Error)
                return
            }

            val userInfoResult = getUserInfoUseCase().firstOrNull()
            val status = if (userInfoResult is DomainResult.Success) {
                userInfoResult.data.memberStatus
            } else {
                MemberStatus.PENDING_AGREE
            }

            _eventFlow.emit(LoginEvent.Success(status))
        } catch (e: Exception) {
            e.printStackTrace()
            _eventFlow.emit(LoginEvent.Error)
        }
    }
}
