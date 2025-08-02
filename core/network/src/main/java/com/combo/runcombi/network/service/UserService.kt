package com.combo.runcombi.network.service

import com.combo.runcombi.network.model.request.DeletePetRequest
import com.combo.runcombi.network.model.request.TermsRequest
import com.combo.runcombi.network.model.response.DefaultResponse
import com.combo.runcombi.network.model.response.UserInfoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserService {
    @POST("api/member/getMemberDetail")
    suspend fun getUserInfo(): Response<UserInfoResponse>

    @POST("api/member/setMemberTerms")
    suspend fun setUserTerms(@Body request: TermsRequest): Response<DefaultResponse>

    @Multipart
    @POST("api/member/setMemberDetail")
    suspend fun setUserInfo(
        @Part("memberDetail") memberDetail: RequestBody,
        @Part("pet") pet: RequestBody,
        @Part memberImage: MultipartBody.Part?,
        @Part petImage: MultipartBody.Part?,
    ): Response<DefaultResponse>

    @Multipart
    @POST("api/member/updateMemberDetail")
    suspend fun updateMemberDetail(
        @Part("updateMemberDetail") updateMemberDetail: RequestBody,
        @Part memberImage: MultipartBody.Part?,
    ): Response<DefaultResponse>

    @Multipart
    @POST("api/pet/updatePetDetail")
    suspend fun updatePetDetail(
        @Part("updatePetDetail") updatePetDetail: RequestBody,
        @Part petImage: MultipartBody.Part?,
    ): Response<DefaultResponse>

    @Multipart
    @POST("api/pet/addPet")
    suspend fun addPet(
        @Part("pet") pet: RequestBody,
        @Part petImage: MultipartBody.Part?,
    ): Response<DefaultResponse>

    @POST("api/pet/deletePet")
    suspend fun deletePet(@Body request: DeletePetRequest): Response<DefaultResponse>
}