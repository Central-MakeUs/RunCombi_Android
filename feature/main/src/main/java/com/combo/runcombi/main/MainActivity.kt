package com.combo.runcombi.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.combo.runcombi.core.designsystem.theme.RunCombiTheme
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.main.component.MainNavigator
import com.combo.runcombi.main.component.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.BLACK, Color.BLACK)
        )
        setContent {
            val navigator: MainNavigator = rememberMainNavigator()

            RunCombiTheme {
                MainScreen(
                    navigator = navigator,
                    startDestination = RouteModel.Login
                )
            }
        }
    }
}

