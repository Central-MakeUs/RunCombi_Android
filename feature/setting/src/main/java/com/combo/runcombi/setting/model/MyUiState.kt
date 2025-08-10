package com.combo.runcombi.setting.model

import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet

data class MyUiState(
    val member: Member? = null,
    val petList: List<Pet> = emptyList(),
    val hasAnnouncement: Boolean = false,
    val isLoading: Boolean = false
) 