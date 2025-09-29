package com.combo.runcombi.wear.data

import android.content.Context
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class WearConnectionChecker @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun checkGooglePlayServices(): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)

        android.util.Log.d("WearConnectionChecker", "Google Play Services 상태: $resultCode")

        return when (resultCode) {
            ConnectionResult.SUCCESS -> {
                android.util.Log.d("WearConnectionChecker", "Google Play Services 사용 가능")
                true
            }

            else -> {
                android.util.Log.e(
                    "WearConnectionChecker",
                    "Google Play Services 사용 불가: $resultCode"
                )
                false
            }
        }
    }

    suspend fun checkWearableConnection(): Boolean {
        return try {
            android.util.Log.d("WearConnectionChecker", "Wearable 연결 상태 확인 중...")

            val nodeClient = Wearable.getNodeClient(context)
            val connectedNodes =
                suspendCancellableCoroutine<List<Node>> { continuation: CancellableContinuation<List<Node>> ->
                    nodeClient.connectedNodes.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            continuation.resume(task.result)
                        } else {
                            continuation.resumeWithException(
                                task.exception ?: Exception("Failed to get connected nodes")
                            )
                        }
                    }
                }

            android.util.Log.d("WearConnectionChecker", "연결된 노드 수: ${connectedNodes.size}")
            connectedNodes.forEach { node ->
                android.util.Log.d(
                    "WearConnectionChecker",
                    "연결된 노드: ${node.displayName} (${node.id})"
                )
            }

            connectedNodes.isNotEmpty()
        } catch (e: Exception) {
            android.util.Log.e("WearConnectionChecker", "Wearable 연결 확인 실패", e)
            false
        }
    }

    suspend fun checkCapability(): Boolean {
        return try {
            android.util.Log.d("WearConnectionChecker", "Capability 확인 중...")

            val capabilityClient = Wearable.getCapabilityClient(context)
            val capabilities =
                suspendCancellableCoroutine { continuation: CancellableContinuation<Map<String, com.google.android.gms.wearable.CapabilityInfo>> ->
                    capabilityClient.getAllCapabilities(com.google.android.gms.wearable.CapabilityClient.FILTER_REACHABLE)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(task.result)
                            } else {
                                continuation.resumeWithException(
                                    task.exception ?: Exception("Failed to get capabilities")
                                )
                            }
                        }
                }

            android.util.Log.d("WearConnectionChecker", "사용 가능한 Capability 수: ${capabilities.size}")
            capabilities.forEach { (name, info) ->
                android.util.Log.d(
                    "WearConnectionChecker",
                    "Capability: $name - 노드 수: ${info.nodes.size}"
                )
            }

            capabilities.isNotEmpty()
        } catch (e: Exception) {
            android.util.Log.e("WearConnectionChecker", "Capability 확인 실패", e)
            false
        }
    }
}
