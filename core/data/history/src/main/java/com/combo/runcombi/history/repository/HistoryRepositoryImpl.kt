package com.combo.runcombi.history.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.common.convert
import com.combo.runcombi.common.handleResult
import com.combo.runcombi.history.mapper.toDomainModel
import com.combo.runcombi.history.model.RunData
import com.combo.runcombi.network.model.request.GetRunDataRequest
import com.combo.runcombi.network.service.HistoryService
import javax.inject.Inject

class HistoryRepositoryImpl @Inject constructor(private val historyService: HistoryService) :
    HistoryRepository {

    override suspend fun getRunData(runId: Int): DomainResult<RunData> = handleResult {
        historyService.getRunData(GetRunDataRequest(runId))
    }.convert {
        it.toDomainModel()
    }

}