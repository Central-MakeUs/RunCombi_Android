package com.combo.runcombi.setting.model

sealed class BottomSheetType {
    data object Logout : BottomSheetType()
    data object Withdraw : BottomSheetType()
} 