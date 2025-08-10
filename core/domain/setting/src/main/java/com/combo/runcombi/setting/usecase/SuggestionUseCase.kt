package com.combo.runcombi.setting.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SuggestionUseCase @Inject constructor(
    private val settingRepository: SettingRepository,
) {
    operator fun invoke(message: String): Flow<DomainResult<Unit>> = flow {
        emit(
            settingRepository.suggestion(message = message)
        )
    }
}