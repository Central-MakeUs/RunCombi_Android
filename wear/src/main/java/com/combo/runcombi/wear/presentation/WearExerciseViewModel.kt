package com.combo.runcombi.wear.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.domain.user.model.Pet
import com.combo.runcombi.walk.model.ExerciseType
import com.combo.runcombi.walk.usecase.MidRunUpdateUseCase
import com.combo.runcombi.walk.usecase.StartRunUseCase
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import android.os.Looper
import android.Manifest
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WearExerciseUiState(
    val isTracking: Boolean = false,
    val isPaused: Boolean = false,
    val startTime: Long = 0L,
    val pausedTime: Long = 0L,
    val totalPausedDuration: Long = 0L,
    val distance: Double = 0.0,
    val currentTime: String = "00:00:00",
    val runId: Int? = null,
    val selectedPetIds: List<Int> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCompleted: Boolean = false,
    val lastLocation: Location? = null,
    val isLocationTracking: Boolean = false,
    val hasLocationPermission: Boolean = false,
    val needsLocationPermission: Boolean = false,
)

@HiltViewModel
class WearExerciseViewModel @Inject constructor(
    private val startRunUseCase: StartRunUseCase,
    private val midRunUpdateUseCase: MidRunUpdateUseCase,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WearExerciseUiState())
    val uiState: StateFlow<WearExerciseUiState> = _uiState.asStateFlow()

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var locationCallback: LocationCallback? = null
    private var lastLocation: Location? = null
    private var totalDistance: Double = 0.0

    init {
        checkLocationPermission()
    }

    fun startExercise(pets: List<Pet>, exerciseType: ExerciseType) {
        viewModelScope.launch {
            android.util.Log.d("WearExerciseViewModel", "=== 운동 시작 요청 ===")
            android.util.Log.d("WearExerciseViewModel", "펫: ${pets.map { it.id }}, 운동타입: ${exerciseType.name}")
            
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val petIds = pets.map { it.id }
                _uiState.value = _uiState.value.copy(selectedPetIds = petIds)

                val result = startRunUseCase(petIds, exerciseType.name)

                when (result) {
                    is DomainResult.Success -> {
                        android.util.Log.d("WearExerciseViewModel", "운동 시작 성공 - runId: ${result.data.runId}")
                        
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isTracking = true,
                            startTime = System.currentTimeMillis(),
                            runId = result.data.runId,
                            distance = 0.0,
                            totalPausedDuration = 0L
                        )
                        
                        android.util.Log.d("WearExerciseViewModel", "운동 시작 성공 후 위치 추적 시작")
                        startLocationTracking()
                    }

                    is DomainResult.Error -> {
                        android.util.Log.e("WearExerciseViewModel", "운동 시작 실패: ${result.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "운동 시작 실패: ${result.message}"
                        )
                    }

                    else -> {
                        android.util.Log.e("WearExerciseViewModel", "알 수 없는 오류")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "알 수 없는 오류"
                        )
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("WearExerciseViewModel", "운동 시작 중 오류 발생", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "운동 시작 중 오류 발생: ${e.message}"
                )
            }
        }
    }

    fun pauseExercise() {
        android.util.Log.d("WearExerciseViewModel", "=== 운동 일시정지 시작 ===")
        android.util.Log.d("WearExerciseViewModel", "일시정지 전 상태 - isPaused: ${_uiState.value.isPaused}, isTracking: ${_uiState.value.isTracking}")
        
        _uiState.value = _uiState.value.copy(
            isPaused = true,
            pausedTime = System.currentTimeMillis()
        )
        
        android.util.Log.d("WearExerciseViewModel", "일시정지 시간 설정: ${_uiState.value.pausedTime}")
        
        stopLocationTracking()
        
        android.util.Log.d("WearExerciseViewModel", "=== 운동 일시정지 완료 ===")
    }

    fun resumeExercise() {
        android.util.Log.d("WearExerciseViewModel", "=== 운동 재개 시작 ===")
        android.util.Log.d("WearExerciseViewModel", "재개 전 상태 - isPaused: ${_uiState.value.isPaused}, totalPausedDuration: ${_uiState.value.totalPausedDuration}ms")
        
        val currentTime = System.currentTimeMillis()
        val pausedDuration = currentTime - _uiState.value.pausedTime
        val newTotalPausedDuration = _uiState.value.totalPausedDuration + pausedDuration
        
        android.util.Log.d("WearExerciseViewModel", "일시정지 시간 계산:")
        android.util.Log.d("WearExerciseViewModel", "  - 현재 시간: ${currentTime}")
        android.util.Log.d("WearExerciseViewModel", "  - 일시정지 시작 시간: ${_uiState.value.pausedTime}")
        android.util.Log.d("WearExerciseViewModel", "  - 이번 일시정지 시간: ${pausedDuration}ms")
        android.util.Log.d("WearExerciseViewModel", "  - 총 일시정지 시간: ${newTotalPausedDuration}ms")
        
        _uiState.value = _uiState.value.copy(
            isPaused = false,
            totalPausedDuration = newTotalPausedDuration,
            pausedTime = 0L
        )
        
        startLocationTracking()
        
        android.util.Log.d("WearExerciseViewModel", "=== 운동 재개 완료 ===")
    }

    fun finishExercise() {
        android.util.Log.d("WearExerciseViewModel", "=== 운동 종료 시작 ===")
        
        viewModelScope.launch {
            val currentState = _uiState.value
            android.util.Log.d("WearExerciseViewModel", "현재 상태 - runId: ${currentState.runId}, isTracking: ${currentState.isTracking}, isPaused: ${currentState.isPaused}")
            
            if (currentState.runId == null) {
                android.util.Log.w("WearExerciseViewModel", "runId가 null이므로 운동 종료 불가")
                return@launch
            }
            
            stopLocationTracking()
            
            if (!currentState.isPaused) {
                android.util.Log.d("WearExerciseViewModel", "운동 중이므로 먼저 일시정지")
                _uiState.value = currentState.copy(isPaused = true)
            }

            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val currentTime = System.currentTimeMillis()
                val totalElapsed = currentTime - currentState.startTime - currentState.totalPausedDuration
                val runTimeSeconds = (totalElapsed / 1000).toInt()
                val runTimeMinutes = (runTimeSeconds / 60).toInt() // 분 단위로 변환

                android.util.Log.d("WearExerciseViewModel", "운동 종료 데이터:")
                android.util.Log.d("WearExerciseViewModel", "  - 시작 시간: ${currentState.startTime}")
                android.util.Log.d("WearExerciseViewModel", "  - 현재 시간: ${currentTime}")
                android.util.Log.d("WearExerciseViewModel", "  - 총 일시정지 시간: ${currentState.totalPausedDuration}ms")
                android.util.Log.d("WearExerciseViewModel", "  - 실제 운동 시간: ${totalElapsed}ms (${runTimeSeconds}초, ${runTimeMinutes}분)")
                android.util.Log.d("WearExerciseViewModel", "  - 총 거리: ${currentState.distance}m")
                android.util.Log.d("WearExerciseViewModel", "  - 펫 ID들: ${currentState.selectedPetIds}")

                android.util.Log.d("WearExerciseViewModel", "midRunUpdate API 호출 시작")
                val midRunUpdateResult = midRunUpdateUseCase(
                    runId = currentState.runId,
                    runTime = runTimeMinutes, // 분 단위로 전송
                    runDistance = currentState.distance
                )

                when (midRunUpdateResult) {
                    is DomainResult.Success -> {
                        android.util.Log.d("WearExerciseViewModel", "midRunUpdate API 호출 성공")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isTracking = false,
                            isPaused = false,
                            isCompleted = true
                        )
                        android.util.Log.d("WearExerciseViewModel", "운동 완료 처리 완료")
                    }
                    is DomainResult.Error -> {
                        android.util.Log.e("WearExerciseViewModel", "midRunUpdate API 호출 실패: ${midRunUpdateResult.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "산책 중간 업데이트 실패: ${midRunUpdateResult.message}"
                        )
                    }
                    else -> {
                        android.util.Log.e("WearExerciseViewModel", "midRunUpdate API 호출 중 알 수 없는 오류")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "산책 중간 업데이트 중 알 수 없는 오류"
                        )
                    }
                }
            } catch (e: Exception) {
                android.util.Log.e("WearExerciseViewModel", "운동 종료 중 오류 발생", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "운동 기록 저장 중 오류 발생: ${e.message}"
                )
            }
        }
        
        android.util.Log.d("WearExerciseViewModel", "=== 운동 종료 완료 ===")
    }

    fun updateTime(timeString: String) {
        _uiState.value = _uiState.value.copy(currentTime = timeString)
    }

    fun updateDistance(distance: Double) {
        _uiState.value = _uiState.value.copy(distance = distance)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private fun checkLocationPermission(): Boolean {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasPermission = hasFineLocation || hasCoarseLocation

        android.util.Log.d("WearExerciseViewModel", "=== 위치 권한 체크 ===")
        android.util.Log.d("WearExerciseViewModel", "FINE_LOCATION: $hasFineLocation")
        android.util.Log.d("WearExerciseViewModel", "COARSE_LOCATION: $hasCoarseLocation")
        android.util.Log.d("WearExerciseViewModel", "권한 있음: $hasPermission")

        _uiState.value = _uiState.value.copy(
            hasLocationPermission = hasPermission,
            needsLocationPermission = !hasPermission
        )

        return hasPermission
    }

    fun requestLocationPermission() {
        android.util.Log.d("WearExerciseViewModel", "위치 권한 요청")
        _uiState.value = _uiState.value.copy(needsLocationPermission = true)
    }

    fun updateLocationPermissionStatus() {
        android.util.Log.d("WearExerciseViewModel", "위치 권한 상태 업데이트")
        checkLocationPermission()
    }

    private fun startLocationTracking() {
        android.util.Log.d("WearExerciseViewModel", "=== 위치 추적 시작 시도 ===")
        android.util.Log.d("WearExerciseViewModel", "현재 상태 - isTracking: ${_uiState.value.isTracking}, isPaused: ${_uiState.value.isPaused}")
        
        if (_uiState.value.isPaused || !_uiState.value.isTracking) {
            android.util.Log.w("WearExerciseViewModel", "위치 추적 조건 불만족 - isPaused: ${_uiState.value.isPaused}, isTracking: ${_uiState.value.isTracking}")
            return
        }

        if (!checkLocationPermission()) {
            android.util.Log.e("WearExerciseViewModel", "위치 권한 없음")
            _uiState.value = _uiState.value.copy(
                error = "위치 권한이 필요합니다.\n메인 화면에서 '위치 권한 허용' 버튼을 눌러주세요.",
                needsLocationPermission = true
            )
            return
        }

        if (!checkLocationService()) {
            android.util.Log.e("WearExerciseViewModel", "위치 서비스 비활성화")
            _uiState.value = _uiState.value.copy(error = "위치 서비스를 활성화해주세요")
            return
        }

        try {
            android.util.Log.d("WearExerciseViewModel", "위치 요청 설정 중...")
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(500)
                .setMaxUpdateDelayMillis(1000)
                .build()

            android.util.Log.d("WearExerciseViewModel", "위치 콜백 설정 중...")
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    android.util.Log.d("WearExerciseViewModel", "=== 위치 콜백 호출됨 ===")
                    android.util.Log.d("WearExerciseViewModel", "위치 개수: ${locationResult.locations.size}")
                    
                    locationResult.locations.forEachIndexed { index, location ->
                        android.util.Log.d("WearExerciseViewModel", "위치 $index: lat=${location.latitude}, lng=${location.longitude}, accuracy=${location.accuracy}m")
                    }
                    
                    locationResult.lastLocation?.let { location ->
                        android.util.Log.d("WearExerciseViewModel", "마지막 위치 사용: lat=${location.latitude}, lng=${location.longitude}")
                        updateLocationData(location)
                    } ?: run {
                        android.util.Log.w("WearExerciseViewModel", "위치 콜백에서 null 위치 수신")
                    }
                }
            }
            
            android.util.Log.d("WearExerciseViewModel", "위치 업데이트 요청 중...")
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback!!,
                Looper.getMainLooper()
            )

            _uiState.value = _uiState.value.copy(isLocationTracking = true)
            android.util.Log.d("WearExerciseViewModel", "=== 위치 추적 요청 성공 ===")
            android.util.Log.d("WearExerciseViewModel", "위치 콜백 등록됨: ${locationCallback != null}")
            
            requestCurrentLocation()
            
        } catch (e: SecurityException) {
            android.util.Log.e("WearExerciseViewModel", "=== 위치 권한 없음 (SecurityException) ===", e)
            _uiState.value = _uiState.value.copy(error = "위치 권한이 필요합니다")
        } catch (e: Exception) {
            android.util.Log.e("WearExerciseViewModel", "=== 위치 추적 시작 실패 ===", e)
            _uiState.value = _uiState.value.copy(error = "위치 추적 시작 실패: ${e.message}")
        }
    }

    private fun stopLocationTracking() {
        android.util.Log.d("WearExerciseViewModel", "=== 위치 추적 중지 시작 ===")
        
        locationCallback?.let { callback ->
            android.util.Log.d("WearExerciseViewModel", "위치 업데이트 제거 중...")
            fusedLocationClient.removeLocationUpdates(callback)
            android.util.Log.d("WearExerciseViewModel", "위치 업데이트 제거 완료")
        } ?: run {
            android.util.Log.w("WearExerciseViewModel", "위치 콜백이 null이므로 제거할 것이 없음")
        }
        
        locationCallback = null
        _uiState.value = _uiState.value.copy(isLocationTracking = false)
        android.util.Log.d("WearExerciseViewModel", "=== 위치 추적 중지 완료 ===")
    }

    private fun updateLocationData(location: Location) {
        android.util.Log.d("WearExerciseViewModel", "=== 위치 데이터 업데이트 시작 ===")
        android.util.Log.d("WearExerciseViewModel", "새로운 위치 - lat=${location.latitude}, lng=${location.longitude}, accuracy=${location.accuracy}m, time=${location.time}")
        android.util.Log.d("WearExerciseViewModel", "현재 상태 - isPaused: ${_uiState.value.isPaused}, isTracking: ${_uiState.value.isTracking}")
        
        if (_uiState.value.isPaused || !_uiState.value.isTracking) {
            android.util.Log.w("WearExerciseViewModel", "일시정지 또는 추적 중단 상태이므로 위치 업데이트 무시")
            return
        }

        lastLocation?.let { prevLocation ->
            val distance = prevLocation.distanceTo(location).toDouble()
            totalDistance += distance
            
            android.util.Log.d("WearExerciseViewModel", "거리 계산:")
            android.util.Log.d("WearExerciseViewModel", "  - 이전 위치: lat=${prevLocation.latitude}, lng=${prevLocation.longitude}")
            android.util.Log.d("WearExerciseViewModel", "  - 현재 위치: lat=${location.latitude}, lng=${location.longitude}")
            android.util.Log.d("WearExerciseViewModel", "  - 추가 거리: ${distance}m")
            android.util.Log.d("WearExerciseViewModel", "  - 총 거리: ${totalDistance}m")
            
            _uiState.value = _uiState.value.copy(
                distance = totalDistance,
                lastLocation = location
            )
            
            android.util.Log.d("WearExerciseViewModel", "UI 상태 업데이트 완료 - 거리: ${_uiState.value.distance}m")
        } ?: run {
            android.util.Log.d("WearExerciseViewModel", "첫 번째 위치 설정 - lat=${location.latitude}, lng=${location.longitude}")
            _uiState.value = _uiState.value.copy(lastLocation = location)
        }

        lastLocation = location
        android.util.Log.d("WearExerciseViewModel", "=== 위치 데이터 업데이트 완료 ===")
    }

    private fun checkLocationService(): Boolean {
        return try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            
            android.util.Log.d("WearExerciseViewModel", "=== 위치 서비스 체크 ===")
            android.util.Log.d("WearExerciseViewModel", "GPS 활성화: $gpsEnabled")
            android.util.Log.d("WearExerciseViewModel", "Network 활성화: $networkEnabled")
            
            val isEnabled = gpsEnabled || networkEnabled
            android.util.Log.d("WearExerciseViewModel", "위치 서비스 활성화됨: $isEnabled")
            
            isEnabled
        } catch (e: Exception) {
            android.util.Log.e("WearExerciseViewModel", "위치 서비스 체크 실패", e)
            true // 체크 실패 시에도 위치 추적 시도
        }
    }

    private fun requestCurrentLocation() {
        android.util.Log.d("WearExerciseViewModel", "=== 현재 위치 즉시 요청 ===")
        
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    android.util.Log.d("WearExerciseViewModel", "마지막 알려진 위치 수신: lat=${location.latitude}, lng=${location.longitude}")
                    updateLocationData(location)
                } else {
                    android.util.Log.w("WearExerciseViewModel", "마지막 알려진 위치가 null")
                }
            }.addOnFailureListener { exception ->
                android.util.Log.e("WearExerciseViewModel", "마지막 위치 요청 실패", exception)
            }
        } catch (e: SecurityException) {
            android.util.Log.e("WearExerciseViewModel", "마지막 위치 요청 권한 없음", e)
        } catch (e: Exception) {
            android.util.Log.e("WearExerciseViewModel", "마지막 위치 요청 실패", e)
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationTracking()
    }
}
