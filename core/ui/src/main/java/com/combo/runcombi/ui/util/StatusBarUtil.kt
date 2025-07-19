package com.combo.runcombi.ui.util

import android.app.Activity
import android.content.Context
import androidx.core.view.WindowCompat

fun isLightStatusBars(context: Activity): Boolean {
    val window = context.window
    val insetsController = WindowCompat.getInsetsController(window, window.decorView)
    return insetsController.isAppearanceLightStatusBars
}

fun setStatusBar(context: Context, isLight: Boolean) {
    if (context is Activity) {
        val window = context.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        if (insetsController.isAppearanceLightStatusBars != isLight)
            insetsController.isAppearanceLightStatusBars = isLight
    }
}
