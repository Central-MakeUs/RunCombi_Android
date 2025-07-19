package com.combo.runcombi.feature.login

import com.combo.runcombi.domain.user.model.MemberStatus

sealed interface LoginEvent {
    data object Error : LoginEvent
    data class Success(val memberStatus: MemberStatus) : LoginEvent
}