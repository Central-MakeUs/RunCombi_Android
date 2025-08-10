package com.combo.runcombi.network.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DeleteDataResponse(
    val result: DeleteDataResult,
) : BaseResponse()