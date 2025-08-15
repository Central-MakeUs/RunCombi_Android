# RunCombi_Android

## 📱 프로젝트 개요

**RunCombi**는 반려동물과 함께하는 운동을 기록하고 관리하는 Android 애플리케이션입니다.

### 주요 기능
- 🐕 반려동물과 함께하는 운동 기록
- 🗺️ GPS 기반 경로 추적
- 👥 사용자 프로필 및 반려동물 관리

## 🛠️ 프로젝트 기술 스택

<div align="center">

![Kotlin](https://img.shields.io/badge/Kotlin-100%25-purple?style=for-the-badge&logo=kotlin)
![Android](https://img.shields.io/badge/Android-API%2026+-green?style=for-the-badge&logo=android)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue?style=for-the-badge)
![Clean Architecture](https://img.shields.io/badge/Architecture-Clean%20Architecture-orange?style=for-the-badge)

</div>

### 📱 Android & Kotlin

| 항목 | 내용 | 버전 |
|------|------|------|
| **언어** | Kotlin | 100% |
| **최소 SDK** | Android 8.0 | API 26 |
| **타겟 SDK** | Android 15 | API 35 |
| **UI 프레임워크** | Jetpack Compose | 최신 |

### 🏗️ 아키텍처 & 패턴

| 패턴 | 설명 | 적용 범위 |
|------|------|-----------|
| **Clean Architecture** | 계층별 관심사 분리 | 전체 프로젝트 |
| **MVVM** | Model-View-ViewModel 패턴 | UI 계층 |
| **Repository Pattern** | 데이터 접근 추상화 | 데이터 계층 |
| **UseCase Pattern** | 비즈니스 로직 캡슐화 | 도메인 계층 |

### 🔧 주요 라이브러리

<div align="center">

![Hilt](https://img.shields.io/badge/DI-Hilt-red?style=flat-square&logo=android)
![Coroutines](https://img.shields.io/badge/Async-Coroutines%20%2B%20Flow-green?style=flat-square&logo=kotlin)
![Navigation](https://img.shields.io/badge/Navigation-Jetpack%20Navigation-blue?style=flat-square&logo=android)
![StateFlow](https://img.shields.io/badge/State-StateFlow-orange?style=flat-square&logo=kotlin)
![DataStore](https://img.shields.io/badge/Storage-DataStore%20%2B%20Room-purple?style=flat-square&logo=android)
![Retrofit](https://img.shields.io/badge/Network-Retrofit%20%2B%20OkHttp-blue?style=flat-square&logo=square)
![Coil](https://img.shields.io/badge/Image-Coil-green?style=flat-square&logo=android)

</div>

| 카테고리 | 라이브러리 | 용도 |
|----------|------------|------|
| **의존성 주입** | Hilt | DI 컨테이너 |
| **비동기 처리** | Kotlin Coroutines + Flow | 비동기 작업 |
| **네비게이션** | Jetpack Navigation Compose | 화면 전환 |
| **상태 관리** | StateFlow, MutableStateFlow | UI 상태 |
| **데이터 저장** | Proto DataStore, Room | 로컬 데이터 |
| **네트워크** | Retrofit, OkHttp | API 통신 |
| **이미지 처리** | Coil | 이미지 로딩 |
| **권한 관리** | Accompanist Permissions | 권한 처리 |

### 📊 외부 서비스

<div align="center">

![Firebase](https://img.shields.io/badge/Analytics-Firebase%20Analytics-orange?style=flat-square&logo=firebase)
![Crashlytics](https://img.shields.io/badge/Crash%20Reporting-Firebase%20Crashlytics-red?style=flat-square&logo=firebase)
![Google Maps](https://img.shields.io/badge/Maps-Google%20Maps%20API-blue?style=flat-square&logo=google-maps)
![Kakao](https://img.shields.io/badge/Login-Kakao%20SDK-yellow?style=flat-square&logo=kakao)

</div>

| 서비스 | 용도 | 통합 방식 |
|--------|------|-----------|
| **Firebase Analytics** | 사용자 행동 분석 | SDK 통합 |
| **Firebase Crashlytics** | 크래시 리포팅 | SDK 통합 |
| **Google Maps API** | 지도 및 위치 서비스 | API 키 |
| **Kakao SDK** | 소셜 로그인 | SDK 통합 |

## 📁 프로젝트 모듈 트리

```
RunCombi_Android/
├── app/                                    # 메인 애플리케이션 모듈
├── build-logic/                           # 빌드 로직 모듈
├── core/                                  # 핵심 공통 모듈
│   ├── analytics/                         # 분석 도구
│   ├── data/                              # 데이터 계층
│   │   ├── auth/                          # 인증 데이터
│   │   ├── common/                        # 공통 데이터
│   │   ├── history/                       # 히스토리 데이터
│   │   ├── setting/                       # 설정 데이터
│   │   ├── user/                          # 사용자 데이터
│   │   ├── walk/                          # 운동 데이터
│   │   ├── datastore/                     # 로컬 데이터 저장소
│   │   └── network/                       # 네트워크 통신
│   ├── designsystem/                      # 디자인 시스템
│   ├── domain/                            # 도메인 계층
│   │   ├── auth/                          # 인증 도메인
│   │   ├── common/                        # 공통 도메인
│   │   ├── history/                       # 히스토리 도메인
│   │   ├── setting/                       # 설정 도메인
│   │   ├── user/                          # 사용자 도메인
│   │   └── walk/                          # 운동 도메인
│   ├── navigation/                        # 네비게이션
│   └── ui/                                # 공통 UI 컴포넌트
└── feature/                               # 기능별 모듈
    ├── history/                           # 운동 히스토리
    ├── login/                             # 로그인/인증
    ├── main/                              # 메인 화면
    ├── setting/                           # 설정
    ├── signup/                            # 회원가입
    └── walk/                              # 운동 추적
```

## 🏭 Flavor 시스템

### Product Flavors
프로젝트는 개발 환경과 프로덕션 환경을 분리하기 위해 두 가지 flavor를 제공합니다.

#### 🧪 Mock Flavor
- **목적**: 개발 및 테스트 환경
- **특징**:
  - 가짜 데이터(Mock Data) 사용
  - 네트워크 API 호출 없이 로컬에서 테스트
  - 빠른 개발 및 디버깅
  - 테스트 데이터로 UI 검증

#### 🚀 Prod Flavor
- **목적**: 실제 프로덕션 환경
- **특징**:
  - 실제 서버 API 연동
  - Firebase 서비스 연동
  - Google Maps API 연동
  - 실제 사용자 데이터 처리


### Flavor별 설정 파일
```
app/
├── src/
│   ├── mock/                    # Mock flavor 전용 소스
│   │   ├── java/
│   │   │   └── com/combo/runcombi/
│   │   │       └── mock/        # Mock 데이터 구현
│   │   └── res/
│   ├── prod/                    # Prod flavor 전용 소스
│   │   ├── java/
│   │   │   └── com/combo/runcombi/
│   │   │       └── prod/        # 실제 서비스 구현
│   │   └── res/
│   └── main/                    # 공통 소스
```


### Flavor 활용 시나리오

#### 🧪 개발 단계 (Mock)
- UI 개발 및 테스트
- 비즈니스 로직 검증
- 네트워크 없이 빠른 반복 개발
- 테스트 데이터로 다양한 시나리오 검증

#### 🚀 배포 단계 (Prod)
- 실제 서버와의 연동 테스트
- 성능 및 안정성 검증
- 실제 사용자 시나리오 테스트
- 스토어 배포용 빌드

## 🔄 CI/CD 파이프라인 흐름

### 📱 Slack 빌드 알림 예시
![RunCombi Android 빌드 성공 알림](./docs/images/slack_notification.png)


```
1. 개발자 코드 Push/PR 생성
   ↓
2. GitHub Actions 자동 트리거
   ↓
3. 자동 빌드 실행
   ↓
4. 빌드 성공 시:
   ├── Firebase Distribution 자동 업로드
   ├── Slack #1-android 채널로 알림 전송
   └── GitHub Release 자동 생성
   ↓
5. QA 팀 및 테스터에게 자동 배포
   ├── Firebase 콘솔에서 APK 다운로드
   ├── 테스트 환경에서 기능 검증
   └── 피드백 수집 및 이슈 등록

```

## 🎨 뷰모델 및 UI 로직 패턴

### 📱 ViewModel 패턴

#### 기본 구조
```kotlin
@HiltViewModel
class ExampleViewModel @Inject constructor(
    private val useCase: ExampleUseCase,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ExampleUiState())
    val uiState: StateFlow<ExampleUiState> = _uiState.asStateFlow()
    
    private val _eventFlow = MutableSharedFlow<ExampleEvent>()
    val eventFlow: SharedFlow<ExampleEvent> = _eventFlow.asSharedFlow()
    
    fun handleAction(action: ExampleAction) {
        viewModelScope.launch {
            when (action) {
                is ExampleAction.Load -> loadData()
                is ExampleAction.Submit -> submitData(action.data)
            }
        }
    }
    
    private suspend fun loadData() {
        _uiState.update { it.copy(isLoading = true) }
        
        try {
            val result = useCase.execute()
            _uiState.update { 
                it.copy(
                    data = result,
                    isLoading = false
                )
            }
        } catch (e: Exception) {
            _uiState.update { 
                it.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}
```

#### UI 상태 관리
```kotlin
data class ExampleUiState(
    val data: List<ExampleData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedItem: ExampleData? = null
)

sealed class ExampleEvent {
    object NavigateToDetail : ExampleEvent()
    object ShowError : ExampleEvent()
    data class ShowToast(val message: String) : ExampleEvent()
}
```

### 🎨 Compose UI 패턴

#### 화면 구조
```kotlin
@Composable
fun ExampleScreen(
    onNavigate: (String) -> Unit,
    viewModel: ExampleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.handleAction(ExampleAction.Load)
    }
    
    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ExampleEvent.NavigateToDetail -> onNavigate("detail")
                is ExampleEvent.ShowError -> { /* 에러 처리 */ }
                is ExampleEvent.ShowToast -> { /* 토스트 표시 */ }
            }
        }
    }
    
    ExampleContent(
        uiState = uiState,
        onAction = viewModel::handleAction
    )
}
```

