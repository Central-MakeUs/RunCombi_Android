package com.combo.runcombi.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.combo.runcombi.core.designsystem.theme.RunCombiTheme
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.usecase.GetUserStatusUseCase
import com.combo.runcombi.main.navigation.MainNavigator
import com.combo.runcombi.main.navigation.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getUserStatusUseCase: GetUserStatusUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.BLACK, Color.BLACK)
        )

        val status = getUserStatusUseCase()


        setContent {
            val navigator: MainNavigator = rememberMainNavigator()

            RunCombiTheme {
                MainScreen(
                    navigator = navigator,
                    startDestination = if (status == MemberStatus.LIVE) RouteModel.MainTab(
                        mainTabDataModel = MainTabDataModel.Walk
                    ) else RouteModel.Login
                )
            }
        }
    }
}

