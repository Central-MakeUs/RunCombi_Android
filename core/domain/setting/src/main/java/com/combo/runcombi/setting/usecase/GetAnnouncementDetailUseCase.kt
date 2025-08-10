package com.combo.runcombi.setting.usecase

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.model.AnnouncementDetail
import com.combo.runcombi.setting.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetAnnouncementDetailUseCase @Inject constructor(
    private val settingRepository: SettingRepository,
) {
    operator fun invoke(id: Int): Flow<DomainResult<AnnouncementDetail>> = flow {
        emit(
            settingRepository.getAnnouncementDetail(id = id)
        )
    }
}