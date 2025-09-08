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

        suspendCancellableCoroutine { continuation ->
            dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                .addOnSuccessListener { dataItem ->
                    continuation.resume(dataItem)
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }
    }

    suspend fun requestMobileLogin() {
        android.util.Log.d("WearDataSyncService", "Creating login request...")
        
        val dataMap = DataMap().apply {
            putString("action", "request_login")
            putLong("timestamp", System.currentTimeMillis())
        }

        val putDataMapRequest = PutDataMapRequest.create("/request_login")
        putDataMapRequest.dataMap.putAll(dataMap)

        android.util.Log.d("WearDataSyncService", "Sending login request to mobile...")

        suspendCancellableCoroutine { continuation ->
            dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                .addOnSuccessListener { dataItem ->
                    android.util.Log.d("WearDataSyncService", "Login request sent successfully")
                    continuation.resume(dataItem)
                }
                .addOnFailureListener { exception ->
                    android.util.Log.e("WearDataSyncService", "Failed to send login request", exception)
                    continuation.resumeWithException(exception)
                }
        }
    }
}
