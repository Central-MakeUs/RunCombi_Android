package com.combo.runcombi.domain.user.model

data class Member(
    val nickname: String,
    val gender: Gender,
    val height: Int,
    val weight: Int,
    val profileImageUrl: String? = null,
)

enum class Gender {
    MALE, FEMALE
}
