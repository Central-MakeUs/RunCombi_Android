# Analytics 모듈

RunCombi_Android 프로젝트의 분석 이벤트 로깅을 위한 모듈입니다.

## 주요 구성 요소

### 1. AnalyticsEvent
분석 이벤트를 정의하는 데이터 클래스입니다.

### 2. AnalyticsHelper
분석 이벤트를 로깅하는 인터페이스입니다.

### 3. StubAnalyticsHelper
개발 및 디버그 빌드에서 사용하는 구현체로, 이벤트를 logcat에 기록합니다.

### 4. FirebaseAnalyticsHelper
프로덕션 빌드에서 사용하는 구현체로, Firebase Analytics에 실제 이벤트를 전송합니다.

### 5. NoOpAnalyticsHelper
테스트와 프리뷰에서 사용하는 구현체로, 아무것도 하지 않습니다.

## 사용법

### 기본 사용법

```kotlin
@Inject
lateinit var analyticsHelper: AnalyticsHelper

analyticsHelper.logEvent(
    AnalyticsEvent(
        type = "custom_event",
        extras = listOf(
            AnalyticsEvent.Param(key = "key", value = "value")
        )
    )
)
```

### 확장 함수 사용

```kotlin
analyticsHelper.logScreenView("LoginActivity")
analyticsHelper.logUserLogin("google")
analyticsHelper.logWalkStarted("outdoor")
analyticsHelper.logSettingChanged("theme", "dark")
analyticsHelper.logFeatureUsed("camera")
analyticsHelper.logButtonClick("login_button", "LoginScreen")
analyticsHelper.logError("network_error", "Connection failed", "MainScreen")
```

### Compose에서 사용

```kotlin
val analyticsHelper = LocalAnalyticsHelper.current

LaunchedEffect(Unit) {
    analyticsHelper.logScreenView("HomeScreen")
}
```

## 표준 이벤트 타입

- `screen_view`: 화면 보기
- `user_login`: 사용자 로그인
- `user_signup`: 사용자 회원가입
- `walk_started`: 산책 시작
- `walk_completed`: 산책 완료
- `setting_changed`: 설정 변경
- `feature_used`: 기능 사용
- `button_click`: 버튼 클릭
- `error_occurred`: 오류 발생
- `app_started`: 앱 시작
- `app_backgrounded`: 앱 백그라운드
- `app_foregrounded`: 앱 포그라운드

## 표준 매개변수 키

- `screen_name`: 화면 이름
- `login_method`: 로그인 방법
- `signup_method`: 회원가입 방법
- `walk_type`: 산책 타입
- `walk_duration`: 산책 시간
- `walk_distance`: 산책 거리
- `setting_name`: 설정 이름
- `setting_value`: 설정 값
- `feature_name`: 기능 이름
- `button_name`: 버튼 이름
- `error_type`: 오류 타입
- `error_message`: 오류 메시지
- `is_new_user`: 신규 사용자 여부
- `user_status`: 사용자 상태
