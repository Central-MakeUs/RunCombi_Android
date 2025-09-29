package com.combo.runcombi.wear.auth

import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.datastore.datasource.AuthDataSource
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.model.UserInfo
import com.combo.runcombi.domain.user.repository.UserRepository
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class WearAuthManager @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userRepository: UserRepository,
) {
    
    var onTokenReceivedCallback: (() -> Unit)? = null
        private set
    
    fun setOnTokenReceivedCallback(callback: () -> Unit) {
        onTokenReceivedCallback = callback
    }

    suspend fun isLoggedIn(): Boolean {
        return try {
            val accessToken = authDataSource.getAccessToken().first()
            accessToken != null && isTokenValid(accessToken)
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserStatus(): MemberStatus {
        return try {
            when (val result = userRepository.getUserInfo()) {
                is DomainResult.Success -> result.data.memberStatus
                else -> MemberStatus.PENDING_AGREE
            }
        } catch (e: Exception) {
            MemberStatus.PENDING_AGREE
        }
    }

    suspend fun getUserInfo(): UserInfo? {
        return try {
            when (val result = userRepository.getUserInfo()) {
                is DomainResult.Success -> result.data
                else -> null
            }
        } catch (e: Exception) {
            null
        }
    }

    suspend fun saveToken(accessToken: String) {
        authDataSource.setAccessToken(accessToken)
    }

    suspend fun clearTokens() {
        authDataSource.deleteAccessToken()
    }

    private suspend fun isTokenValid(token: String): Boolean {
        return try {
            // 토큰 유효성 검증을 위해 간단한 API 호출 시도
            val result = userRepository.getUserInfo()
            result is DomainResult.Success
        } catch (e: Exception) {
            false
        }
    }
}

@AndroidEntryPoint
class WearDataListenerService : WearableListenerService() {

    @Inject
    lateinit var wearAuthManager: WearAuthManager

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        
        android.util.Log.d("WearDataListenerService", "onDataChanged called with ${dataEvents.count} events")

        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                android.util.Log.d("WearDataListenerService", "Data changed: ${dataItem.uri.path}")

                when (dataItem.uri.path) {
                    "/auth_token" -> {
                        android.util.Log.d("WearDataListenerService", "Received auth_token")
                        val dataMap = DataMapItem.fromDataItem(dataItem).dataMap
                        val accessToken = dataMap.getString("access_token")
                        
                        if (accessToken != null) {
                            android.util.Log.d("WearDataListenerService", "Saving access token")
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    wearAuthManager.saveToken(accessToken)
                                    android.util.Log.d("WearDataListenerService", "Token saved successfully")
                                    // 토큰 저장 성공 시 콜백 호출
                                    wearAuthManager.onTokenReceivedCallback?.invoke()
                                } catch (e: Exception) {
                                    android.util.Log.e("WearDataListenerService", "Failed to save token", e)
                                }
                            }
                        } else {
                            android.util.Log.w("WearDataListenerService", "Access token is null")
                        }
                    }
                    "/request_login" -> {
                        android.util.Log.d("WearDataListenerService", "Received request_login (ignoring - should be handled by mobile)")
                    }
                    else -> {
                        android.util.Log.d("WearDataListenerService", "Unknown path: ${dataItem.uri.path}")
                    }
                }
            }
        }
    }
}
