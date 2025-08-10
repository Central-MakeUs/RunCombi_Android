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
                    announcementType = "NOTICE",
                    endDate = "2024-12-31",
                    isRead = "Y",
                    regDate = "2024-01-01",
                    startDate = "2024-01-01",
                    title = "런컴비 앱 업데이트 안내"
                ),
                Announcement(
                    announcementId = 2,
                    announcementType = "EVENT",
                    endDate = "2024-12-31",
                    isRead = "N",
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
                announcementImageUrl = "https://ontariospca.ca/wp-content/uploads/2024/09/EmilyJeanPhotography-0016-scaled.jpg",
                announcementType = "EVENT",
                code = "NOTICE_001",
                content = "런컴비 앱이 업데이트되었습니다. 새로운 기능을 확인해보세요!\n반려견은 최고의 운동 파트너라는 사실, 알고 계셨나요?\n" +
                        "귀찮기만 했던 산책은 이제 그만!\n" +
                        "반려견과 함께 운동 콤비가 되어, 건강해지고 추억도 쌓는 의미 있는 시간으로 바꿔보세요.\u2028특별한 운동 경험, 지금 바로 런콤비에서 시작해보세요!\n" +
                        "\n" +
                        "■ 런콤비만의 특별한 기능 ■\n" +
                        "\n" +
                        "# 나와 댕댕이, 함께 하는 운동 시간\n" +
                        "운동을 시작하면 나와 반려견의 운동 현황이 동시에 보여요.\u2028나는 얼마나 걸었고, 우리 아이는 얼마나 움직였는지,\u2028각자의 칼로리 소모량까지 한눈에 확인할 수 있어요.\u2028반려견과 함께하는 운동, 생각보다 훨씬 더 특별해요!\n" +
                        "\n" +
                        "# 산책 코스 + 추억까지 예쁘게 기록\n" +
                        "운동이 끝나면 함께 지나온 길이 지도 이미지로 저장돼요.\u2028사진도 찍어 그날의 특별한 순간까지 남겨보세요!\u2028운동 기록으로 우리의 추억을 되돌아보는 재미, 놓치지 마세요.\n" +
                        "\n" +
                        "# 우리 콤비의 운동, 캘린더로 한눈에\n" +
                        "이번 달 우리가 얼마나 열심히 운동했는지 궁금하시죠?\u2028런콤비가 깔끔한 통계로 한눈에 보여드려요! \n" +
                        "운동한 날은 캘린더에 귀여운 발자국이 남아요. \n" +
                        "함께 쌓아가는 우리의 발자국이 이렇게 뿌듯할 일인가요?! \n" +
                        "\n" +
                        "# 칼로리 소모, 음식으로 재밌게 보여드려요\n" +
                        "나와 반려견의 신체 정보를 바탕으로 각자의 소모 칼로리를 계산해요.\u2028그걸 나와 반려견이 좋아하는 음식으로 환산해 보여주니까,\u2028운동에 대한 뿌듯함이 더욱 커져요!\n" +
                        "\n" +
                        "■ 이런 보호자라면 꼭 써보세요!\n" +
                        "운동은 하고 싶은데 혼자 하기 싫은 분\n" +
                        "산책을 더 재미있고 의미 있게 만들고 싶은 분\n" +
                        "나와 반려견의 건강을 함께 챙기고 싶은 분\n" +
                        "\n" +
                        "■ 런콤비와 함께하면 이런 변화가!\n" +
                        "동기부여 UP: 혼자보다 콤비랑 함께니까 더 즐거워요\n" +
                        "건강함 UP: 나도, 반려견도 함께 건강해져요\n" +
                        "뿌듯함 UP: 운동 기록이 쌓일수록 성취감이 커져요\n" +
                        "추억 UP: 하루하루 특별한 순간이 기록으로 남아요\n" +
                        "\n" +
                        "■ 앱 접근 권한 안내\n" +
                        "원활한 서비스 이용을 위해 다음 권한이 필요합니다:\n" +
                        "(필수) GPS 위치정보: 운동 거리 및 코스 측정\n" +
                        "(선택) 카메라: 운동 중 사진 촬영\n" +
                        "(선택) 저장소: 사진 및 기록 저장\n" +
                        "※ 선택 권한은 동의하지 않아도 기본 기능은 이용 가능합니다.",
                endDate = "2024-12-31",
                eventApplyUrl = "https://www.naver.com/",
                regDate = "2024-01-01",
                startDate = "2024-01-01",
                title = "런컴비 앱 업데이트 안내"
            )
        )
    }
}
