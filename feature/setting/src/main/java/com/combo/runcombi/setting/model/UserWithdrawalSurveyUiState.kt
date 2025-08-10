package com.combo.runcombi.setting.model

enum class WithdrawalReason(val reason: String) {
    RECORD_CLEANUP("기록을 정리하고 싶어요"),
    DIFFICULT_USAGE("사용 방법이 어려워요"),
    INSUFFICIENT_FEATURES("기능이 부족하거나 불편했어요"),
    RARELY_USED("자주 쓰지 않게 되었어요"),
    OTHER("기타");
    
    val displayName: String get() = reason
}

data class UserWithdrawalSurveyUiState(
    val selectedReason: List<WithdrawalReason> = emptyList(),
    val additionalReason: String = "",
    val isLoading: Boolean = false,
)
