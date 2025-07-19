package com.combo.runcombi.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.combo.runcombi.auth.usecase.GetIsNewUserUseCase
import com.combo.runcombi.core.designsystem.theme.RunCombiTheme
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.main.navigation.MainNavigator
import com.combo.runcombi.main.navigation.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getIsNewUserUseCase: GetIsNewUserUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.BLACK, Color.BLACK)
        )

        val isNewUser = getIsNewUserUseCase()

        setContent {
            val navigator: MainNavigator = rememberMainNavigator()

            RunCombiTheme {
                MainScreen(
                    navigator = navigator,
                    startDestination = if (isNewUser) RouteModel.Login else RouteModel.MainTab(
                        mainTabDataModel = MainTabDataModel.Walk
                    )
                )
            }
        }
    }
}

