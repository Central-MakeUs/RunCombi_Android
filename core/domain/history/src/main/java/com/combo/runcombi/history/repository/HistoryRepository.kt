package com.combo.runcombi.history.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.history.model.RunData


interface HistoryRepository {
    suspend fun getRunData(runId: Int): DomainResult<RunData>

}
