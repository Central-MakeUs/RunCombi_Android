package com.combo.runcombi.analytics

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "Analytics"

@Singleton
internal class StubAnalyticsHelper @Inject constructor() : AnalyticsHelper {
    override fun logEvent(event: AnalyticsEvent) {
        val extrasStr = if (event.extras.isNotEmpty()) {
            event.extras.joinToString(", ") { "${it.key}=${it.value}" }
        } else ""
        
        Log.d(TAG, "[${event.type}] $extrasStr")
    }
}
