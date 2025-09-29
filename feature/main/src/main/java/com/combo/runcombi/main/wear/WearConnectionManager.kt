package com.combo.runcombi.main.wear

import com.combo.runcombi.auth.usecase.GetAccessTokenUseCase
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import javax.inject.Inject


class WearConnectionManager @Inject constructor(
    private val wearSyncService: WearSyncService,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) {

    suspend fun syncUserDataToWear() {
        try {
            val accessToken = getAccessTokenUseCase()

            if (accessToken != null) {
                android.util.Log.d("WearConnectionManager", "Syncing access token to wear")
                wearSyncService.sendTokensToWear(accessToken)
                android.util.Log.d("WearConnectionManager", "Access token synced successfully")
            } else {
                android.util.Log.w("WearConnectionManager", "No access token available to sync")
            }
        } catch (e: Exception) {
            android.util.Log.e("WearConnectionManager", "Failed to sync user data to wear", e)
        }
    }

}

class MobileWearDataListenerService : WearableListenerService() {

    private lateinit var dataClient: com.google.android.gms.wearable.DataClient

    override fun onCreate() {
        super.onCreate()
        android.util.Log.d("MobileWearDataListenerService", "Service created")
        
        try {
            dataClient = com.google.android.gms.wearable.Wearable.getDataClient(this)
            android.util.Log.d("MobileWearDataListenerService", "DataClient initialized")
        } catch (e: Exception) {
            android.util.Log.e("MobileWearDataListenerService", "Failed to initialize DataClient", e)
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        
        android.util.Log.d("MobileWearDataListenerService", "onDataChanged called with ${dataEvents.count} events")

        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                android.util.Log.d("MobileWearDataListenerService", "Data changed: ${dataItem.uri.path}")

                when (dataItem.uri.path) {
                    "/request_login" -> {
                        android.util.Log.d("MobileWearDataListenerService", "Received login request from wear")
                        // 워치에서 로그인 요청을 받았을 때
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                sendTokenToWear()
                                android.util.Log.d("MobileWearDataListenerService", "Token sent to wear")
                            } catch (e: Exception) {
                                android.util.Log.e("MobileWearDataListenerService", "Failed to send token to wear", e)
                            }
                        }
                    }
                }
            }
        }
    }
    
    private suspend fun sendTokenToWear() {
        try {
            android.util.Log.d("MobileWearDataListenerService", "Sending token to wear...")
            
            // 실제 토큰을 가져오는 로직 (SharedPreferences 또는 다른 저장소에서)
            val accessToken = getStoredAccessToken()
            
            if (accessToken != null) {
                val dataMap = com.google.android.gms.wearable.DataMap().apply {
                    putString("access_token", accessToken)
                    putLong("timestamp", System.currentTimeMillis())
                }

                val putDataMapRequest = com.google.android.gms.wearable.PutDataMapRequest.create("/auth_token")
                putDataMapRequest.dataMap.putAll(dataMap)

                android.util.Log.d("MobileWearDataListenerService", "Sending auth token to wear device...")

                kotlinx.coroutines.suspendCancellableCoroutine<com.google.android.gms.wearable.DataItem> { continuation ->
                    dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                        .addOnSuccessListener { dataItem ->
                            android.util.Log.d("MobileWearDataListenerService", "Auth token sent successfully to wear")
                            continuation.resume(dataItem)
                        }
                        .addOnFailureListener { exception ->
                            android.util.Log.e("MobileWearDataListenerService", "Failed to send auth token to wear", exception)
                            continuation.resumeWithException(exception)
                        }
                }
            } else {
                android.util.Log.w("MobileWearDataListenerService", "No access token available to send")
            }
        } catch (e: Exception) {
            android.util.Log.e("MobileWearDataListenerService", "Failed to send token to wear", e)
        }
    }
    
    private fun getStoredAccessToken(): String? {
        // 고정 토큰 사용 (개발용)
        val fixedToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE3NTkxNDg3ODYsInN1YiI6IjEzMiIsImV4cCI6MTc1OTc1MzU4Niwicm9sZSI6IlVTRVIifQ.rlUVKbI7BFqcm3W2JryWvqVtOt2PtYiS_qoz7Elcw_hgzlYiwWjaZlAZk1PAwRJmhl0VdddFVOzhd-mle6rRQA"
        android.util.Log.d("MobileWearDataListenerService", "고정 토큰 사용: ${fixedToken.take(20)}...")
        return fixedToken
    }
}
