package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MemberModel(
    val email: String? = null,
    val gender: String? = null,
    val height: Double? = null,
    val isActive: String? = null,
    val memberId: Int? = null,
    val memberTerms: List<String>? = null,
    val nickname: String? = null,
    val profileImgKey: String? = null,
    val profileImgUrl: String? = null,
    val weight: Double? = null,
)