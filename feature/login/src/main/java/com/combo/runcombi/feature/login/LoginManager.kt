package com.combo.runcombi.feature.login

import android.content.Context

sealed class LoginData {
    data class Success(val email: String) : LoginData()
    data class Failed(val exception: Exception) : LoginData()
}

class LoginManager(
    private val context: Context,
) {
}
