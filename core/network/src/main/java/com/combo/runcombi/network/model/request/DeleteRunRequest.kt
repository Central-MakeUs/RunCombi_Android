package com.combo.runcombi.network.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteRunRequest(val runId: Int)