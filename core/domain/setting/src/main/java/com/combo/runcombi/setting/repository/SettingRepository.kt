package com.combo.runcombi.setting.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.model.DeleteData

interface SettingRepository {
    suspend fun getDeleteData(): DomainResult<DeleteData>

    suspend fun suggestion(message: String): DomainResult<Unit>

    suspend fun accountDeletionReason(reason: List<String>): DomainResult<Unit>

    suspend fun checkVersion(version: String): DomainResult<Boolean>
}
