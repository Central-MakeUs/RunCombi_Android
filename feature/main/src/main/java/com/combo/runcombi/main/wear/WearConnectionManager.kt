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
            
            // 연결된 워치 확인
            checkConnectedWearables()
        } catch (e: Exception) {
            android.util.Log.e("MobileWearDataListenerService", "Failed to initialize DataClient", e)
        }
    }
    
    private fun checkConnectedWearables() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val nodeClient = com.google.android.gms.wearable.Wearable.getNodeClient(this@MobileWearDataListenerService)
                val connectedNodes = suspendCancellableCoroutine<List<com.google.android.gms.wearable.Node>> { continuation ->
                    nodeClient.connectedNodes.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(task.result)
                        } else {
                            continuation.resumeWithException(task.exception ?: Exception("Failed to get connected nodes"))
                        }
                    }
                }
                
                android.util.Log.d("MobileWearDataListenerService", "연결된 워치 수: ${connectedNodes.size}")
                connectedNodes.forEach { node ->
                    android.util.Log.d("MobileWearDataListenerService", "연결된 워치: ${node.displayName} (${node.id})")
                }
                
                if (connectedNodes.isEmpty()) {
                    android.util.Log.w("MobileWearDataListenerService", "연결된 워치가 없습니다")
                }
            } catch (e: Exception) {
                android.util.Log.e("MobileWearDataListenerService", "워치 연결 확인 실패", e)
            }
        }
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        
        android.util.Log.d("MobileWearDataListenerService", "onDataChanged called with ${dataEvents.count} events")

        dataEvents.forEach { event ->
            if (event.type == DataEvent.TYPE_CHANGED && event.dataItem.uri.path == "/authRequest") {
                android.util.Log.d("MobileWearDataListenerService", "Received auth request from wear")
                
                // 로그인 상태 확인
                val isLoggedIn = checkLoginStatus()
                android.util.Log.d("MobileWearDataListenerService", "Login status: $isLoggedIn")

                if (isLoggedIn) {
                    // 토큰이 있으면 토큰 전송
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            sendTokenToWear()
                            android.util.Log.d("MobileWearDataListenerService", "Token sent to wear")
                        } catch (e: Exception) {
                            android.util.Log.e("MobileWearDataListenerService", "Failed to send token to wear", e)
                        }
                    }
                } else {
                    // 로그인 상태 응답 전송
                    val putDataReq = com.google.android.gms.wearable.PutDataMapRequest.create("/authStatus").apply {
                        dataMap.putBoolean("isLoggedIn", false)
                    }.asPutDataRequest().setUrgent()

                    dataClient.putDataItem(putDataReq).addOnSuccessListener {
                        android.util.Log.d("MobileWearDataListenerService", "Login status response sent to watch")
                    }.addOnFailureListener { e ->
                        android.util.Log.e("MobileWearDataListenerService", "Failed to send login status response", e)
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
    
    private fun checkLoginStatus(): Boolean {
        val token = getStoredAccessToken()
        val isLoggedIn = token != null
        android.util.Log.d("MobileWearDataListenerService", "Login status check: $isLoggedIn")
        return isLoggedIn
    }
    
    private fun getStoredAccessToken(): String? {
        // SharedPreferences에서 토큰을 가져오는 로직
        val sharedPref = getSharedPreferences("auth_prefs", android.content.Context.MODE_PRIVATE)
        val token = sharedPref.getString("access_token", null)
        android.util.Log.d("MobileWearDataListenerService", "저장된 토큰 확인: ${token != null}")
        if (token != null) {
            android.util.Log.d("MobileWearDataListenerService", "토큰: ${token.take(20)}...")
        } else {
            android.util.Log.w("MobileWearDataListenerService", "저장된 토큰이 없습니다")
        }
        return token
    }
}
