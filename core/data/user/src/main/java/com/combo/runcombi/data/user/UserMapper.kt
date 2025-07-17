package com.combo.runcombi.data.user

import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.Member
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
            userStatus = memberStatus.orEmpty())
    }
}

fun MemberModel.toDomainModel(): Member {
    return Member(
        nickname = nickname ?: "",
        gender = if (gender == "male") Gender.MALE else Gender.FEMALE,
        height = height ?: 0,
        weight = weight ?: 0,
        profileImageUrl = profileImgUrl
    )
}

fun Member.toDataModel(): MemberModel {
    return MemberModel(
        nickname = nickname,
        gender = gender.name,
        height = height,
        weight = weight
    )
}

fun PetModel.toDomainModel(): Pet {
    return Pet(
        name = name ?: "",
        age = age ?: 0,
        weight = weight ?: 0.0,
        runStyle = RunStyle.RELAXED,
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



