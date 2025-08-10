package com.combo.runcombi.setting.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckVersionUseCase @Inject constructor(
    private val settingRepository: SettingRepository,
) {
    operator fun invoke(version: String): Flow<DomainResult<Boolean>> = flow {
        emit(
            settingRepository.checkVersion(version = version)
        )
    }
}
