package com.combo.runcombi.walk.service

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.combo.runcombi.walk.model.ExerciseType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalkTrackingServiceHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "WalkTrackingServiceHelper"
    }

    fun startTracking(exerciseType: ExerciseType) {
        Log.d(TAG, "startTracking: 운동 추적 시작 - exerciseType=${exerciseType.name}")
        
        val intent = Intent(context, WalkTrackingService::class.java).apply {
            action = WalkTrackingService.ACTION_START_TRACKING
            putExtra(WalkTrackingService.EXTRA_EXERCISE_TYPE, exerciseType.name)
        }

        try {
            context.startForegroundService(intent)
            Log.d(TAG, "startTracking: Foreground 서비스 시작 요청 성공")
        } catch (e: Exception) {
            Log.e(TAG, "startTracking: Foreground 서비스 시작 실패", e)
        }
    }

    fun stopTracking() {
        Log.d(TAG, "stopTracking: 운동 추적 중지")
        val intent = Intent(context, WalkTrackingService::class.java).apply {
            action = WalkTrackingService.ACTION_STOP_TRACKING
        }
        try {
            context.startService(intent)
            Log.d(TAG, "stopTracking: 서비스 중지 요청 성공")
        } catch (e: Exception) {
            Log.e(TAG, "stopTracking: 서비스 중지 요청 실패", e)
        }
    }

    fun pauseTracking() {
        Log.d(TAG, "pauseTracking: 운동 추적 일시정지")
        val intent = Intent(context, WalkTrackingService::class.java).apply {
            action = WalkTrackingService.ACTION_PAUSE_TRACKING
        }
        try {
            context.startService(intent)
            Log.d(TAG, "pauseTracking: 서비스 일시정지 요청 성공")
        } catch (e: Exception) {
            Log.e(TAG, "pauseTracking: 서비스 일시정지 요청 실패", e)
        }
    }

    fun resumeTracking() {
        Log.d(TAG, "resumeTracking: 운동 추적 재개")
        val intent = Intent(context, WalkTrackingService::class.java).apply {
            action = WalkTrackingService.ACTION_RESUME_TRACKING
        }
        try {
            context.startService(intent)
            Log.d(TAG, "resumeTracking: 서비스 재개 요청 성공")
        } catch (e: Exception) {
            Log.e(TAG, "resumeTracking: 서비스 재개 요청 실패", e)
        }
    }

    fun restartNotification() {
        Log.d(TAG, "restartNotification: 알림 재시작")
        val intent = Intent(context, WalkTrackingService::class.java).apply {
            action = WalkTrackingService.ACTION_RESTART_NOTIFICATION
        }
        try {
            context.startService(intent)
            Log.d(TAG, "restartNotification: 알림 재시작 요청 성공")
        } catch (e: Exception) {
            Log.e(TAG, "restartNotification: 알림 재시작 요청 실패", e)
        }
    }

    fun isTracking(): Boolean {
        Log.d(TAG, "isTracking: 서비스 상태 확인 중")
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)
        
        val isRunning = runningServices.any { service ->
            service.service.className == WalkTrackingService::class.java.name
        }
        
        Log.d(TAG, "isTracking: 서비스 실행 상태 = $isRunning, 총 실행 중인 서비스 수 = ${runningServices.size}")
        return isRunning
    }
}
