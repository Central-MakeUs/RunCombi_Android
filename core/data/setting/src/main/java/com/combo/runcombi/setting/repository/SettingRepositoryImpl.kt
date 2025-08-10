package com.combo.runcombi.setting.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.common.convert
import com.combo.runcombi.common.handleResult
import com.combo.runcombi.network.model.request.CheckVersionRequest
import com.combo.runcombi.network.model.request.LeaveReasonRequest
import com.combo.runcombi.network.model.request.SuggestionRequest
import com.combo.runcombi.network.service.SettingService
import com.combo.runcombi.setting.mapper.toDomainModel
import com.combo.runcombi.setting.model.DeleteData
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(private val settingService: SettingService) :
    SettingRepository {

    override suspend fun getDeleteData(): DomainResult<DeleteData> = handleResult {
        settingService.getDeleteData()
    }.convert {
        it.toDomainModel()
    }

    override suspend fun suggestion(message: String): DomainResult<Unit> = handleResult {
        settingService.suggestion(
            SuggestionRequest(
                sggMsg = message
            )
        )
    }.convert {}

    override suspend fun accountDeletionReason(reason: List<String>): DomainResult<Unit> =
        handleResult {
            settingService.leaveReason(
                LeaveReasonRequest(
                    reason = reason
                )
            )
        }.convert {}

    override suspend fun checkVersion(version: String): DomainResult<Boolean> = handleResult {
        settingService.checkVersion(CheckVersionRequest(os = "Android", version = version))
    }.convert {
        it.toDomainModel()
    }
}