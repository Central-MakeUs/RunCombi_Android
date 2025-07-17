package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResult(
    val member: MemberModel,
    val memberStatus: String?,
    val petList: List<PetModel>?
)