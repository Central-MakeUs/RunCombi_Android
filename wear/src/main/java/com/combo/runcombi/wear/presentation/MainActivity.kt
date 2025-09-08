package com.combo.runcombi.wear.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.wear.presentation.theme.RunCombi_AndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    RunCombi_AndroidTheme {
        val viewModel: WearAuthViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            TimeText()
            
            if (uiState.isLoggedIn && uiState.memberStatus == MemberStatus.LIVE && uiState.userInfo != null) {
                WearMainScreen(userInfo = uiState.userInfo!!)
            } else {
                WearAuthScreen(viewModel = viewModel)
            }
        }
    }
}