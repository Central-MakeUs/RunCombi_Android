package com.combo.runcombi.data.setting.mapper

import com.combo.runcombi.network.model.response.AnnouncementDetailResponse
import com.combo.runcombi.network.model.response.AnnouncementResponse
import com.combo.runcombi.network.model.response.AnnouncementResult
import com.combo.runcombi.network.model.response.CheckVersionResponse
import com.combo.runcombi.network.model.response.DeleteDataResponse
import com.combo.runcombi.setting.model.Announcement
import com.combo.runcombi.setting.model.AnnouncementDetail
import com.combo.runcombi.setting.model.DeleteData

fun DeleteDataResponse.toDomainModel(): DeleteData {
    return with(result) {
        DeleteData(
            resultPetName = resultPetName ?: emptyList(),
            resultRun = resultRun ?: 0,
            resultRunImage = resultRunImage ?: 0
        )
    }
}

fun CheckVersionResponse.toDomainModel(): Boolean {
    return with(result) {
        updateRequire == "Y"
    }
}

fun AnnouncementDetailResponse.toDomainModel(): AnnouncementDetail {
    return with(result) {
        AnnouncementDetail(
            announcementId = announcementId ?: 0,
            announcementImageUrl = announcementImageUrl ?: "",
            announcementType = announcementType ?: "",
            code = code ?: "",
            content = content ?: "",
            endDate = endDate ?: "",
            eventApplyUrl = eventApplyUrl ?: "",
            regDate = regDate ?: "",
            startDate = startDate ?: "",
            title = title ?: ""
        )
    }
}

fun AnnouncementResponse.toDomainModel(): List<Announcement> {
    return with(result) {
        result.map {
            it.toDomainModel()
        }
    }
}

fun AnnouncementResult.toDomainModel(): Announcement {
    return Announcement(
        announcementId = announcementId ?: 0,
        announcementType = announcementType ?: "",
        endDate = endDate ?: "",
        isRead = isRead ?: "",
        regDate = regDate ?: "",
        startDate = startDate ?: "",
        title = title ?: ""
    )
}
