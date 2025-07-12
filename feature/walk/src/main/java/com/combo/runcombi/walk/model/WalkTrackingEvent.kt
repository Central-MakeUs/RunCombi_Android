package com.combo.runcombi.walk.model

sealed interface WalkTrackingEvent {
    data class ShowBottomSheet(val type: BottomSheetType) : WalkTrackingEvent
}

enum class BottomSheetType {
    FINISH, CANCEL, NONE
}

data class BottomSheetContent(
    val title: String,
    val subtitle: String,
    val acceptButtonText: String,
    val cancelButtonText: String
)

fun getBottomSheetContent(type: BottomSheetType): BottomSheetContent? {
    return when (type) {
        BottomSheetType.FINISH -> BottomSheetContent(
            title = "오늘 운동은 여기서 마무리할까요?",
            subtitle = "종료하면 지금까지의 기록이 저장됩니다.",
            acceptButtonText = "종료",
            cancelButtonText = "아니요"
        )
        BottomSheetType.CANCEL -> BottomSheetContent(
            title = "정말 운동을 취소하시겠어요?",
            subtitle = "취소하면 지금까지의 기록이 저장되지 않아요.",
            acceptButtonText = "취소",
            cancelButtonText = "아니요"
        )
        else -> null
    }
} 