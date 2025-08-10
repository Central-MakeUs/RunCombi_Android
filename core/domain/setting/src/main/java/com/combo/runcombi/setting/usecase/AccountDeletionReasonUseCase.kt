package com.combo.runcombi.setting.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccountDeletionReasonUseCase @Inject constructor(
    private val settingRepository: SettingRepository,
) {
    operator fun invoke(reason: List<String>): Flow<DomainResult<Unit>> = flow {
        emit(
            settingRepository.accountDeletionReason(reason = reason)
        )
    }
}
