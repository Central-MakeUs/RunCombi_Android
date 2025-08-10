package com.combo.runcombi.data.setting.repository

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.setting.model.Announcement
import com.combo.runcombi.setting.model.AnnouncementDetail
import com.combo.runcombi.setting.model.DeleteData
import com.combo.runcombi.setting.repository.SettingRepository
import javax.inject.Inject

class MockSettingRepositoryImpl @Inject constructor() : SettingRepository {
    override suspend fun getDeleteData(): DomainResult<DeleteData> {
        return DomainResult.Success(
            DeleteData(
                resultPetName = listOf("초코", "루시"),
                resultRun = 15,
                resultRunImage = 8
            )
        )
    }

    override suspend fun suggestion(message: String): DomainResult<Unit> {
        return DomainResult.Success(Unit)
    }

    override suspend fun accountDeletionReason(reason: List<String>): DomainResult<Unit> {
        return DomainResult.Success(Unit)
    }

    override suspend fun checkVersion(version: String): DomainResult<Boolean> {
        return DomainResult.Success(false)
    }

    override suspend fun getAnnouncementList(): DomainResult<List<Announcement>> {
        return DomainResult.Success(
            listOf(
                Announcement(
                    announcementId = 1,
                    announcementType = "공지",
                    endDate = "2024-12-31",
                    isRead = "N",
                    regDate = "2024-01-01",
                    startDate = "2024-01-01",
                    title = "런컴비 앱 업데이트 안내"
                ),
                Announcement(
                    announcementId = 2,
                    announcementType = "이벤트",
                    endDate = "2024-12-31",
                    isRead = "Y",
                    regDate = "2024-01-01",
                    startDate = "2024-01-01",
                    title = "신규 사용자 이벤트"
                )
            )
        )
    }

    override suspend fun getAnnouncementDetail(id: Int): DomainResult<AnnouncementDetail> {
        return DomainResult.Success(
            AnnouncementDetail(
                announcementId = id,
                announcementImageUrl = "https://example.com/image.jpg",
                announcementType = "공지",
                code = "NOTICE_001",
                content = "런컴비 앱이 업데이트되었습니다. 새로운 기능을 확인해보세요!",
                endDate = "2024-12-31",
                eventApplyUrl = "https://www.naver.com/",
                regDate = "2024-01-01",
                startDate = "2024-01-01",
                title = "런컴비 앱 업데이트 안내"
            )
        )
    }
}
