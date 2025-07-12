package com.combo.runcombi.domain.user.model

data class User(
    val nickname: String,
    val gender: Gender,
    val height: Int,
    val weight: Int,
    val profileImageUrl: String? = null,
)
