package com.combo.runcombi.wear.data

import android.content.Context
import com.combo.runcombi.wear.auth.WearTokenManager
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.WearableListenerService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WearTokenReceiver : WearableListenerService() {

    @Inject
    lateinit var wearTokenManager: WearTokenManager

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        super.onDataChanged(dataEvents)
        
        android.util.Log.d("WearTokenReceiver", "onDataChanged called with ${dataEvents.count} events")

        for (event in dataEvents) {
            if (event.type == DataEvent.TYPE_CHANGED) {
                val dataItem = event.dataItem
                android.util.Log.d("WearTokenReceiver", "Data changed: ${dataItem.uri.path}")

                when (dataItem.uri.path) {
                    "/auth_token" -> {
                        android.util.Log.d("WearTokenReceiver", "=== 모바일로부터 인증 토큰 수신 ===")
                        val dataMapItem = DataMapItem.fromDataItem(dataItem)
                        val dataMap = dataMapItem.dataMap
                        
                        val accessToken = dataMap.getString("access_token")
                        val timestamp = dataMap.getLong("timestamp")
                        
                        android.util.Log.d("WearTokenReceiver", "토큰 정보:")
                        android.util.Log.d("WearTokenReceiver", "  - 토큰: ${accessToken?.take(20)}...")
                        android.util.Log.d("WearTokenReceiver", "  - 타임스탬프: $timestamp")
                        
                        if (accessToken != null) {
                            android.util.Log.d("WearTokenReceiver", "로컬 저장소에 토큰 저장 중...")
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    wearTokenManager.saveAccessTokenFromMobile(accessToken)
                                    android.util.Log.d("WearTokenReceiver", "=== 토큰 저장 성공 ===")
                                } catch (e: Exception) {
                                    android.util.Log.e("WearTokenReceiver", "=== 토큰 저장 실패 ===", e)
                                }
                            }
                        } else {
                            android.util.Log.w("WearTokenReceiver", "모바일로부터 null 토큰 수신")
                        }
                    }
                }
            }
        }
    }
}
