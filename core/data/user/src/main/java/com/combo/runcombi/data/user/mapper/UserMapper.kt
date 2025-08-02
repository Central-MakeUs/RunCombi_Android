package com.combo.runcombi.data.user.mapper

import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.domain.user.model.UserInfo
import com.combo.runcombi.network.model.response.MemberModel
import com.combo.runcombi.network.model.response.PetModel
import com.combo.runcombi.network.model.response.UserInfoResponse

fun UserInfoResponse.toDomainModel(): UserInfo {
    return with(result) {
        UserInfo(
            member = member.toDomainModel(),
            petList = petList?.map { it.toDomainModel() } ?: emptyList(),
            memberStatus = runCatching {
                enumValueOf<MemberStatus>(memberStatus ?: "")
            }.getOrNull() ?: MemberStatus.PENDING_AGREE
        )
    }
}

fun MemberModel.toDomainModel(): Member {
    return Member(
        nickname = nickname ?: "",
        gender = if (gender == Gender.MALE.name) Gender.MALE else Gender.FEMALE,
        height = height?.toInt() ?: 0,
        weight = weight?.toInt() ?: 0,
        profileImageUrl = profileImgUrl
    )
}

fun Member.toDataModel(): MemberModel {
    return MemberModel(
        nickname = nickname,
        gender = gender.name,
        height = height.toDouble(),
        weight = weight.toDouble()
    )
}

fun PetModel.toDomainModel(): Pet {
    return Pet(
        id = petId ?: 0,
        name = name ?: "",
        age = age ?: 0,
        weight = weight ?: 0.0,
        runStyle = RunStyle.RUNNING,
        profileImageUrl = petImageUrl
    )
}

fun Pet.toDataModel(): PetModel {
    return PetModel(
        age = age,
        name = name,
        runStyle = runStyle.name,
        weight = weight
    )
}



