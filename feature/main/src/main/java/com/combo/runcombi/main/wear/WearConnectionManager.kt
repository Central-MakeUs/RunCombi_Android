package com.combo.runcombi.main.wear

// 워치 연동 비활성화
/*
import com.combo.runcombi.auth.usecase.GetAccessTokenUseCase
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


class WearConnectionManager @Inject constructor(
    private val wearSyncService: WearSyncService,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) {

    suspend fun syncUserDataToWear() {
        try {
            val accessToken = getAccessTokenUseCase()

            if (accessToken != null) {
                wearSyncService.sendTokensToWear(accessToken)
            }
        } catch (e: Exception) {
            // 에러 처리
        }
    }

}

@AndroidEntryPoint
class MobileWearDataListenerService : WearableListenerService() {

    @Inject
    lateinit var wearConnectionManager: WearConnectionManager

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
                                wearConnectionManager.syncUserDataToWear()
                                android.util.Log.d("MobileWearDataListenerService", "User data synced to wear")
                            } catch (e: Exception) {
                                android.util.Log.e("MobileWearDataListenerService", "Failed to sync user data", e)
                            }
                        }
                    }
                }
            }
        }
    }
}
*/
