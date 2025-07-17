package com.combo.runcombi.feature.login.model

sealed class LoginData {
    data class Success(val token: String) : LoginData()
    data class Failed(val exception: Exception) : LoginData()
}