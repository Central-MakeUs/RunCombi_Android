package com.combo.runcombi.wear.data

import android.content.Context
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataMap
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WearDataSyncService @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataClient: DataClient = Wearable.getDataClient(context)

    suspend fun sendTokenToWear(accessToken: String) {
        val dataMap = DataMap().apply {
            putString("access_token", accessToken)
            putLong("timestamp", System.currentTimeMillis())
        }

        val putDataMapRequest = PutDataMapRequest.create("/auth_token")
        putDataMapRequest.dataMap.putAll(dataMap)

        android.util.Log.d("WearDataSyncService", "Sending token to wear: ${accessToken.take(20)}...")

        suspendCancellableCoroutine { continuation ->
            dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                .addOnSuccessListener { dataItem ->
                    android.util.Log.d("WearDataSyncService", "Token sent successfully to wear")
                    continuation.resume(dataItem)
                }
                .addOnFailureListener { exception ->
                    android.util.Log.e("WearDataSyncService", "Failed to send token to wear", exception)
                    continuation.resumeWithException(exception)
                }
        }
    }

    suspend fun requestMobileLogin() {
        android.util.Log.d("WearDataSyncService", "=== 모바일 로그인 요청 시작 ===")
        android.util.Log.d("WearDataSyncService", "DataClient 상태 확인 중...")
        
        val dataMap = DataMap().apply {
            putString("action", "request_login")
            putLong("timestamp", System.currentTimeMillis())
        }

        val putDataMapRequest = PutDataMapRequest.create("/request_login")
        putDataMapRequest.dataMap.putAll(dataMap)

        android.util.Log.d("WearDataSyncService", "로그인 요청 데이터 생성 완료")
        android.util.Log.d("WearDataSyncService", "경로: /request_login")
        android.util.Log.d("WearDataSyncService", "액션: request_login")
        android.util.Log.d("WearDataSyncService", "타임스탬프: ${System.currentTimeMillis()}")

        android.util.Log.d("WearDataSyncService", "모바일로 로그인 요청 전송 중...")

        suspendCancellableCoroutine { continuation ->
            dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                .addOnSuccessListener { dataItem ->
                    android.util.Log.d("WearDataSyncService", "=== 로그인 요청 전송 성공 ===")
                    android.util.Log.d("WearDataSyncService", "DataItem URI: ${dataItem.uri}")
                    continuation.resume(dataItem)
                }
                .addOnFailureListener { exception ->
                    android.util.Log.e("WearDataSyncService", "=== 로그인 요청 전송 실패 ===", exception)
                    continuation.resumeWithException(exception)
                }
        }
    }
}
