package com.combo.runcombi.data.user.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.common.convert
import com.combo.runcombi.common.handleResult
import com.combo.runcombi.data.user.mapper.toDataModel
import com.combo.runcombi.data.user.mapper.toDomainModel
import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.domain.user.model.UserInfo
import com.combo.runcombi.domain.user.repository.UserRepository
import com.combo.runcombi.network.model.request.DeletePetRequest
import com.combo.runcombi.network.model.request.TermsRequest
import com.combo.runcombi.network.service.UserService
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userService: UserService) :
    UserRepository {
    override suspend fun getUserInfo(): DomainResult<UserInfo> = handleResult {
        userService.getUserInfo()
    }.convert {
        it.toDomainModel()
    }

    override suspend fun setUserTerms(agreeTerms: List<String>): DomainResult<Unit> = handleResult {
        val request = TermsRequest(agreeTerms)
        userService.setUserTerms(request)
    }.convert {}

    override suspend fun setUserInfo(
        memberDetail: Member,
        petDetail: Pet,
        memberImage: File?,
        petImage: File?,
    ): DomainResult<Unit> = handleResult {
        val json = Json { ignoreUnknownKeys = true }
        val memberDetailJsonString = json.encodeToString(memberDetail.toDataModel())
        val petDetailJsonString = json.encodeToString(petDetail.toDataModel())

        val memberDetailBody =
            memberDetailJsonString.toRequestBody("application/json".toMediaTypeOrNull())
        val petDetailBody =
            petDetailJsonString.toRequestBody("application/json".toMediaTypeOrNull())

        val memberImagePart = memberImage?.let {
            MultipartBody.Part.createFormData(
                "memberImage", it.name, it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }
        val petImagePart = petImage?.let {
            MultipartBody.Part.createFormData(
                "petImage", it.name, it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        userService.setUserInfo(
            memberDetail = memberDetailBody,
            pet = petDetailBody,
            memberImage = memberImagePart,
            petImage = petImagePart
        )
    }.convert {}

    override suspend fun updateMemberDetail(
        memberDetail: Member,
        memberImage: File?,
    ): DomainResult<Unit> = handleResult {
        val json = Json { ignoreUnknownKeys = true }
        val memberDetailJsonString = json.encodeToString(memberDetail.toDataModel())

        val memberDetailBody =
            memberDetailJsonString.toRequestBody("application/json".toMediaTypeOrNull())

        val memberImagePart = memberImage?.let {
            MultipartBody.Part.createFormData(
                "memberImage", it.name, it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        userService.updateMemberDetail(memberDetailBody, memberImagePart)

    }.convert { }

    override suspend fun updatePet(petDetail: Pet, petImage: File?): DomainResult<Unit> =
        handleResult {
            val json = Json { ignoreUnknownKeys = true }
            val petDetailJsonString = json.encodeToString(petDetail.toDataModel())

            val petDetailBody =
                petDetailJsonString.toRequestBody("application/json".toMediaTypeOrNull())


            val petImagePart = petImage?.let {
                MultipartBody.Part.createFormData(
                    "petImage", it.name, it.asRequestBody("image/*".toMediaTypeOrNull())
                )
            }

            userService.updatePetDetail(petDetailBody, petImagePart)
        }.convert { }

    override suspend fun addPet(
        petDetail: Pet,
        petImage: File?,
    ): DomainResult<Unit> = handleResult {
        val json = Json { ignoreUnknownKeys = true }
        val petDetailJsonString = json.encodeToString(petDetail.toDataModel())

        val petDetailBody =
            petDetailJsonString.toRequestBody("application/json".toMediaTypeOrNull())


        val petImagePart = petImage?.let {
            MultipartBody.Part.createFormData(
                "petImage", it.name, it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }

        userService.addPet(petDetailBody, petImagePart)
    }.convert {}

    override suspend fun deletePet(petId: Int): DomainResult<Unit> = handleResult {
        userService.deletePet(DeletePetRequest(petId))
    }.convert { }

}