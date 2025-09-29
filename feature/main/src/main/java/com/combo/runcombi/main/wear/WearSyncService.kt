package com.combo.runcombi.main.wear

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
class WearSyncService @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataClient: DataClient = Wearable.getDataClient(context)

    suspend fun sendTokensToWear(accessToken: String) {
        android.util.Log.d("WearSyncService", "Sending tokens to wear...")
        
        val dataMap = DataMap().apply {
            putString("access_token", accessToken)
            putLong("timestamp", System.currentTimeMillis())
        }

        val putDataMapRequest = PutDataMapRequest.create("/auth_token")
        putDataMapRequest.dataMap.putAll(dataMap)

        android.util.Log.d("WearSyncService", "Sending auth token to wear device...")

        suspendCancellableCoroutine { continuation ->
            dataClient.putDataItem(putDataMapRequest.asPutDataRequest())
                .addOnSuccessListener { dataItem ->
                    android.util.Log.d("WearSyncService", "Auth token sent successfully to wear")
                    continuation.resume(dataItem)
                }
                .addOnFailureListener { exception ->
                    android.util.Log.e("WearSyncService", "Failed to send auth token to wear", exception)
                    continuation.resumeWithException(exception)
                }
        }
    }
}
