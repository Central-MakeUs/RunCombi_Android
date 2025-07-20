package com.combo.runcombi.walk.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.common.convert
import com.combo.runcombi.common.handleResult
import com.combo.runcombi.network.model.request.StartRunRequest
import com.combo.runcombi.network.model.response.MemberRunData
import com.combo.runcombi.network.model.response.PetRunData
import com.combo.runcombi.network.service.WalkService
import com.combo.runcombi.walk.mapper.toDataModel
import com.combo.runcombi.walk.model.WalkPet
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class WalkRepositoryImpl @Inject constructor(private val walkService: WalkService) :
    WalkRepository {
    override suspend fun startRun(petList: List<Int>, memberRunStyle: String): DomainResult<Int> =
        handleResult {
            walkService.requestStartRun(
                StartRunRequest(
                    petList = petList,
                    memberRunStyle = memberRunStyle
                )
            )
        }.convert {
            it.result.runId
        }

    override suspend fun endRun(
        runId: Int,
        memberCal: Int,
        runTime: Int,
        runDistance: Double,
        memo: String,
        runEvaluating: String,
        petList: List<WalkPet>,
        routeImage: File?,
        runImage: File?,
    ): DomainResult<Unit> = handleResult {
        val memberRunData = MemberRunData(
            runId = runId,
            memberCal = memberCal,
            runTime = runTime,
            runDistance = runDistance,
            runEvaluating = runEvaluating,
            memo = memo
        )
        val petRunData = PetRunData(
            petCalList = petList.map { it.toDataModel() }
        )
        val json = Json { ignoreUnknownKeys = true }
        val memberRunDataJsonString = json.encodeToString(memberRunData)
        val petRunDataJsonString = json.encodeToString(petRunData)

        val memberRunDataBody =
            memberRunDataJsonString.toRequestBody("application/json".toMediaTypeOrNull())

        val petRunDataBody =
            petRunDataJsonString.toRequestBody("application/json".toMediaTypeOrNull())

        val routeImagePart = routeImage?.let {
            MultipartBody.Part.createFormData(
                "routeImage", it.name, it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }
        val runImagePart = runImage?.let {
            MultipartBody.Part.createFormData(
                "runImage", it.name, it.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }
        walkService.requestEndRun(
            memberRunData = memberRunDataBody,
            petRunData = petRunDataBody,
            routeImage = routeImagePart,
            runImage = runImagePart
        )
    }.convert {}
}