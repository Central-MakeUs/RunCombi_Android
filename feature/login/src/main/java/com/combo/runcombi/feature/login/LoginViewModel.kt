package com.combo.runcombi.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.auth.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<LoginEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun login(loginData: LoginData) {
        viewModelScope.launch {
            when (loginData) {
                is LoginData.Success -> {
                    try {
                        if (BuildConfig.DEBUG) {
                            _eventFlow.emit(LoginEvent.Success)
                        } else {
                            val result = loginUseCase.requestKakaoLogin(token = loginData.token)
                            _eventFlow.emit(
                                result?.let {
                                    LoginEvent.Success
                                } ?: run {
                                    LoginEvent.Error
                                }
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                is LoginData.Failed -> _eventFlow.emit(LoginEvent.Error)
            }
        }
    }
}
