package com.combo.runcombi.domain.user.model

data class Pet(
    val id: Int = 0,
    val name: String,
    val age: Int,
    val weight: Double,
    val runStyle: RunStyle,
    val profileImageUrl: String? = null,
)

/**
 * RUNNING - 에너지가 넘쳐요
 * WALKING - 여유롭게 걸어요
 * SLOW_WALKING - 천천히 걸으며 자주 쉬어요
 *
 * activityFactor: 활동 계수 (칼로리 연산 시 사용)
 * */
enum class RunStyle(val activityFactor: Double) {
    RUNNING(6.4),
    WALKING(4.8),
    SLOW_WALKING(3.2);

    companion object {
        fun fromOrDefault(name: String, default: RunStyle = RunStyle.RUNNING): RunStyle {
            return try {
                valueOf(name)
            } catch (e: IllegalArgumentException) {
                default
            }
        }
    }
}
