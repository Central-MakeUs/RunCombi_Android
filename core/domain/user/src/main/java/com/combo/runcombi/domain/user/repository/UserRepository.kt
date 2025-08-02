package com.combo.runcombi.domain.user.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.UserInfo
import java.io.File

interface UserRepository {
    suspend fun getUserInfo(): DomainResult<UserInfo>

    suspend fun setUserTerms(agreeTerms: List<String>): DomainResult<Unit>

    suspend fun setUserInfo(
        memberDetail: Member,
        petDetail: Pet,
        memberImage: File?,
        petImage: File?,
    ): DomainResult<Unit>

    suspend fun updateMemberDetail(memberDetail: Member, memberImage: File?): DomainResult<Unit>

    suspend fun updatePet(petDetail: Pet, petImage: File?): DomainResult<Unit>

    suspend fun addPet(petDetail: Pet, petImage: File?): DomainResult<Unit>

    suspend fun deletePet(petId: Int): DomainResult<Unit>
}
