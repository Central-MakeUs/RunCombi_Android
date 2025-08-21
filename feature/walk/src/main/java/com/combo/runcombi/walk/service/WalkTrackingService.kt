package com.combo.runcombi.walk.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.combo.runcombi.walk.model.LocationPoint
import com.combo.runcombi.walk.usecase.CalculateMemberCalorieUseCase
import com.combo.runcombi.walk.usecase.CalculatePetCalorieUseCase
import com.combo.runcombi.walk.usecase.UpdateWalkRecordUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class WalkTrackingService : Service() {

    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "walk_tracking_channel"
        private const val CHANNEL_NAME = "운동 추적"
        private const val CHANNEL_DESCRIPTION = "운동 중 위치 추적을 위한 알림"

        const val ACTION_START_TRACKING = "com.combo.runcombi.START_TRACKING"
        const val ACTION_STOP_TRACKING = "com.combo.runcombi.STOP_TRACKING"
        const val ACTION_PAUSE_TRACKING = "com.combo.runcombi.PAUSE_TRACKING"
        const val ACTION_RESUME_TRACKING = "com.combo.runcombi.RESUME_TRACKING"
        const val ACTION_RESTART_NOTIFICATION = "com.combo.runcombi.RESTART_NOTIFICATION"

        const val EXTRA_EXERCISE_TYPE = "exercise_type"

        private const val TAG = "WalkTrackingService"
    }

    @Inject
    lateinit var updateWalkRecordUseCase: UpdateWalkRecordUseCase

    @Inject
    lateinit var calculateMemberCalorieUseCase: CalculateMemberCalorieUseCase

    @Inject
    lateinit var calculatePetCalorieUseCase: CalculatePetCalorieUseCase

    @Inject
    lateinit var dataManager: WalkTrackingDataManager

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var timeUpdateJob: Job? = null

    private var lastPoint: LocationPoint? = null
    private var speedList: List<Double> = emptyList()

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: 서비스 생성 시작")
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        setupLocationCallback()
        Log.d(TAG, "onCreate: 서비스 생성 완료")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand: action=${intent?.action}, startId=$startId")

        when (intent?.action) {
            ACTION_START_TRACKING -> {
                val exerciseType =
                    intent.getStringExtra(EXTRA_EXERCISE_TYPE) ?: return START_NOT_STICKY
                Log.d(TAG, "onStartCommand: 운동 추적 시작 - exerciseType=$exerciseType")
                startForegroundTracking(exerciseType)
            }

            ACTION_STOP_TRACKING -> {
                Log.d(TAG, "onStartCommand: 운동 추적 중지")
                stopTracking()
            }

            ACTION_PAUSE_TRACKING -> {
                Log.d(TAG, "onStartCommand: 운동 추적 일시정지")
                pauseTracking()
            }

            ACTION_RESUME_TRACKING -> {
                Log.d(TAG, "onStartCommand: 운동 추적 재개")
                resumeTracking()
            }

            ACTION_RESTART_NOTIFICATION -> {
                Log.d(TAG, "onStartCommand: 알림 재시작")
                restartNotification()
            }

            else -> {
                Log.w(TAG, "onStartCommand: 알 수 없는 action=${intent?.action}")
            }
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundTracking(exerciseType: String) {
        Log.d(TAG, "startForegroundTracking: 시작 - exerciseType=$exerciseType")
        try {
            dataManager.updateExerciseType(exerciseType)
            dataManager.updateTrackingState(true)
            Log.d(TAG, "startForegroundTracking: 데이터 매니저 업데이트 완료")

            startLocationUpdates()
            startTimeUpdates()
            Log.d(TAG, "startForegroundTracking: 위치 및 시간 업데이트 시작")

            startForeground(NOTIFICATION_ID, createNotification())
            Log.d(TAG, "startForegroundTracking: Foreground 서비스 시작 완료")
        } catch (e: Exception) {
            Log.e(TAG, "startForegroundTracking: 오류 발생", e)
            stopSelf()
        }
    }

    private fun stopTracking() {
        Log.d(TAG, "stopTracking: 운동 추적 중지 및 서비스 종료")
        dataManager.updateTrackingState(false)
        stopLocationUpdates()
        stopTimeUpdates()
        stopForeground(true)
        stopSelf()
    }

    private fun pauseTracking() {
        dataManager.updatePauseState(true)
        stopLocationUpdates()
        stopTimeUpdates()
    }

    private fun resumeTracking() {
        dataManager.updatePauseState(false)
        startLocationUpdates()
        startTimeUpdates()
    }

    private fun startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates: 위치 업데이트 시작")
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(500)
            .setMaxUpdateDelayMillis(1000)
            .build()

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            Log.d(TAG, "startLocationUpdates: 위치 업데이트 요청 성공")
        } catch (e: SecurityException) {
            Log.e(TAG, "startLocationUpdates: 위치 권한 없음", e)
        } catch (e: Exception) {
            Log.e(TAG, "startLocationUpdates: 위치 업데이트 요청 실패", e)
        }
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    updateLocationData(location)
                }
            }
        }
    }

    private fun updateLocationData(location: Location) {
        val newPoint = LocationPoint(
            location.latitude,
            location.longitude,
            location.time,
            location.accuracy
        )

        Log.d(
            TAG,
            "updateLocationData: 새로운 위치 - lat=${location.latitude}, lng=${location.longitude}, accuracy=${location.accuracy}"
        )

        val currentPathPoints = dataManager.trackingData.value.pathPoints
        val newPathPoints = currentPathPoints + com.google.android.gms.maps.model.LatLng(
            location.latitude,
            location.longitude
        )

        when (val result = updateWalkRecordUseCase(lastPoint, newPoint, speedList)) {
            is com.combo.runcombi.common.DomainResult.Success -> {
                val data = result.data
                val currentDistance = dataManager.trackingData.value.distance
                val newDistance = currentDistance + data.distance

                Log.d(
                    TAG,
                    "updateLocationData: 거리 업데이트 - 기존: ${currentDistance}m, 추가: ${data.distance}m, 새로운: ${newDistance}m"
                )

                updateCalories(newDistance)

                dataManager.updateLocationData(
                    newPathPoints,
                    newDistance,
                    dataManager.trackingData.value.time
                )

                lastPoint = newPoint
                speedList =
                    (speedList + (if (lastPoint != null) data.distance / ((newPoint.timestamp - (lastPoint?.timestamp
                        ?: newPoint.timestamp)).coerceAtLeast(1000L) / 1000.0) else 0.0)).takeLast(
                        100
                    )

                Log.d(
                    TAG,
                    "updateLocationData: 성공적으로 업데이트됨 - 총 경로점: ${newPathPoints.size}, 속도 리스트 크기: ${speedList.size}"
                )
            }

            is com.combo.runcombi.common.DomainResult.Error, is com.combo.runcombi.common.DomainResult.Exception -> {
                Log.w(TAG, "updateLocationData: UseCase 실행 실패 - ${result.javaClass.simpleName}")

                updateCalories(dataManager.trackingData.value.distance)

                dataManager.updateLocationData(
                    newPathPoints,
                    dataManager.trackingData.value.distance,
                    dataManager.trackingData.value.time
                )
                lastPoint = newPoint
            }
        }
    }

    private fun updateCalories(distanceMeters: Double) {
        val distanceKm = distanceMeters / 1000.0
        val rounded = BigDecimal(distanceKm).setScale(2, RoundingMode.HALF_UP).toDouble()

        val trackingData = dataManager.trackingData.value
        val exerciseType = try {
            com.combo.runcombi.walk.model.ExerciseType.valueOf(trackingData.exerciseType)
        } catch (e: IllegalArgumentException) {
            com.combo.runcombi.walk.model.ExerciseType.WALKING
        }

        // 멤버 칼로리 계산
        val memberUiModel = trackingData.member?.let { member ->
            val memberCalorie = calculateMemberCalorieUseCase(
                exerciseType,
                member.member.gender.name,
                member.member.weight.toDouble(),
                rounded
            ).roundToInt()
            member.copy(calorie = memberCalorie)
        }

        val petUiModelList = trackingData.petList?.map { petUiModel ->
            val pet = petUiModel.pet
            val petCalorie = calculatePetCalorieUseCase(
                pet.weight,
                rounded,
                pet.runStyle.activityFactor
            ).roundToInt()
            petUiModel.copy(calorie = petCalorie)
        }

        dataManager.updateMemberData(memberUiModel)
        dataManager.updatePetListData(petUiModelList)

        Log.d(
            TAG,
            "updateCalories: 칼로리 업데이트 완료 - 거리: ${rounded}km, 멤버칼로리: ${memberUiModel?.calorie}, 펫칼로리: ${petUiModelList?.map { it.calorie }}"
        )
    }

    private fun startTimeUpdates() {
        timeUpdateJob = serviceScope.launch {
            while (isActive) {
                delay(1000)
                if (!dataManager.trackingData.value.isPaused) {
                    val currentTime = dataManager.trackingData.value.time
                    dataManager.updateLocationData(
                        dataManager.trackingData.value.pathPoints,
                        dataManager.trackingData.value.distance,
                        currentTime + 1
                    )
                }
            }
        }
    }

    private fun stopTimeUpdates() {
        timeUpdateJob?.cancel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = CHANNEL_DESCRIPTION
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("운동 기록 중")
            .setSmallIcon(com.combo.runcombi.core.designsystem.R.drawable.ic_walk_selected)
            .setOngoing(true)
            .build()
    }

    private fun restartNotification() {
        try {
            // 현재 알림을 제거하고 새로운 알림으로 교체
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(NOTIFICATION_ID)
            
            // 새로운 알림으로 포그라운드 서비스 재시작
            startForeground(NOTIFICATION_ID, createNotification())
            Log.d(TAG, "restartNotification: 알림 재시작 완료")
        } catch (e: Exception) {
            Log.e(TAG, "restartNotification: 알림 재시작 실패", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        stopLocationUpdates()
        stopTimeUpdates()
    }
}
