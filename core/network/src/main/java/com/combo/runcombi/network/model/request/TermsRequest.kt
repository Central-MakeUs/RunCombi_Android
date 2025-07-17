package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class TermsRequest(
    val agreeTermList: List<String>
)