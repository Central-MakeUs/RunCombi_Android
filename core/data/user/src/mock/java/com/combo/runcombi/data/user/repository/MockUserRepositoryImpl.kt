package com.combo.runcombi.data.user.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Gender
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.RunStyle
import com.combo.runcombi.domain.user.model.UserInfo
import com.combo.runcombi.domain.user.repository.UserRepository
import java.io.File
import javax.inject.Inject

class MockUserRepositoryImpl @Inject constructor() : UserRepository {
    override suspend fun getUserInfo(): DomainResult<UserInfo> {
        return DomainResult.Success(
            UserInfo(
                member = Member(
                    nickname = "창스",
                    gender = Gender.MALE,
                    height = 180,
                    weight = 80,
                    profileImageUrl = null
                ),
                petList = listOf(
                    Pet(
                        name = "초코",
                        age = 10,
                        weight = 4.0,
                        runStyle = RunStyle.SLOW_WALKING,
                        profileImageUrl = null
                    )
                ),
                memberStatus = MemberStatus.LIVE,
            )
        )
    }

    override suspend fun setUserTerms(agreeTerms: List<String>): DomainResult<Unit> {
        return DomainResult.Success(Unit)
    }

    override suspend fun setUserInfo(
        memberDetail: Member,
        petDetail: Pet,
        memberImage: File?,
        petImage: File?,
    ): DomainResult<Unit> {
        return DomainResult.Success(Unit)
    }

    override suspend fun updateMemberDetail(
        memberDetail: Member,
        memberImage: File?,
    ): DomainResult<Unit> {
        return DomainResult.Success(Unit)
    }

    override suspend fun updatePet(petDetail: Pet, petImage: File?): DomainResult<Unit> {
        return DomainResult.Success(Unit)
    }

    override suspend fun addPet(petDetail: Pet, petImage: File?): DomainResult<Unit> {
        return DomainResult.Success(Unit)
    }

    override suspend fun deletePet(petId: Int): DomainResult<Unit> {
        return DomainResult.Success(Unit)
    }
} 