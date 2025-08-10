package com.combo.runcombi.setting.model

data class UserWithdrawalInfoUiState(
    val isLoading: Boolean = false,
    val petList: List<String> = emptyList(),
    val imageCount: Int = 0,
    val runCount: Int = 0

)
