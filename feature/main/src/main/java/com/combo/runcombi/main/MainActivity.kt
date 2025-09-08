package com.combo.runcombi.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.combo.runcombi.auth.usecase.GetAccessTokenUseCase
import com.combo.runcombi.core.designsystem.theme.RunCombiTheme
import com.combo.runcombi.core.navigation.model.MainTabDataModel
import com.combo.runcombi.core.navigation.model.RouteModel
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.domain.user.usecase.GetUserStatusUseCase
import com.combo.runcombi.main.navigation.MainNavigator
import com.combo.runcombi.main.navigation.rememberMainNavigator
import com.combo.runcombi.main.wear.WearConnectionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getUserStatusUseCase: GetUserStatusUseCase

    @Inject
    lateinit var getAccessTokenUseCase: GetAccessTokenUseCase

    @Inject
    lateinit var wearConnectionManager: WearConnectionManager

    private var status: MemberStatus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.BLACK, Color.BLACK)
        )

        val isNew = getAccessTokenUseCase() == null

        if (!isNew) {
            status = getUserStatusUseCase()

            // Wear 동기화 및 데이터 전송
            if (status == MemberStatus.LIVE) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        wearConnectionManager.syncUserDataToWear()
                    } catch (e: Exception) {
                        android.util.Log.e("MainActivity", "Wear 동기화 실패", e)
                    }
                }
            }
        }

        setContent {
            val navigator: MainNavigator = rememberMainNavigator()

            RunCombiTheme {
                MainScreen(
                    navigator = navigator,
                    startDestination = if (!isNew && status == MemberStatus.LIVE) RouteModel.MainTab(
                        mainTabDataModel = MainTabDataModel.Walk
                    ) else RouteModel.Login
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}

