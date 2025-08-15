package com.combo.runcombi.analytics

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("MissingPermission")
@Singleton
internal class FirebaseAnalyticsHelper @Inject constructor(
    @ApplicationContext private val context: Context
) : AnalyticsHelper {
    
    private val firebaseAnalytics: FirebaseAnalytics by lazy {
        FirebaseAnalytics.getInstance(context)
    }
    
    override fun logEvent(event: AnalyticsEvent) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, event.type)
            
            event.extras.forEach { param ->
                putString(param.key, param.value)
            }
        }
        
        firebaseAnalytics.logEvent(event.type, bundle)
    }
}
