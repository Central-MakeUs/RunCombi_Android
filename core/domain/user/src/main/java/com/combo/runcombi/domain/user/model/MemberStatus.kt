package com.combo.runcombi.domain.user.model

/**
 * 1) PENDING_AGREE : 약관 동의를 진행하지 않은 회원
 * 2) PENDING_MEMBER_DETAIL : 약관 동의는 진행하였지만 회원 정보를 기입하지 않은 회원
 * 3) LIVE : 회원가입이 모두 끝난 정상 회원
 * */
enum class MemberStatus {
    PENDING_AGREE, PENDING_MEMBER_DETAIL, LIVE
}