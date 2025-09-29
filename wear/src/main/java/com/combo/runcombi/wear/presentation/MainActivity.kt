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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.TimeText
import com.combo.runcombi.domain.user.model.MemberStatus
import com.combo.runcombi.walk.model.ExerciseType
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
        var currentScreen by remember { mutableStateOf("main") }
        var selectedPets by remember {
            mutableStateOf<List<com.combo.runcombi.domain.user.model.Pet>>(
                emptyList()
            )
        }
        var selectedExerciseType by remember { mutableStateOf<ExerciseType?>(null) }
        var exerciseResult by remember { mutableStateOf<ExerciseResult?>(null) }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            TimeText()

            when {
                uiState.isLoggedIn && uiState.memberStatus == MemberStatus.LIVE && uiState.userInfo != null -> {
                    when (currentScreen) {
                        "main" -> {
                            WearMainScreen(
                                userInfo = uiState.userInfo!!,
                                onStartExercise = { currentScreen = "petSelect" },
                            )
                        }

                        "petSelect" -> {
                            WearPetSelectScreen(
                                userInfo = uiState.userInfo!!,
                                onStartExercise = { pets ->
                                    selectedPets = pets
                                    currentScreen = "exerciseType"
                                },
                                onBack = { currentScreen = "main" }
                            )
                        }

                        "exerciseType" -> {
                            WearExerciseTypeSelectScreen(
                                selectedPets = selectedPets,
                                onExerciseTypeSelected = { exerciseType ->
                                    selectedExerciseType = exerciseType
                                    currentScreen = "exercise"
                                },
                                onBack = { currentScreen = "petSelect" }
                            )
                        }

                        "exercise" -> {
                            selectedExerciseType?.let { exerciseType ->
                                WearExerciseTrackingScreen(
                                    member = uiState.userInfo!!.member,
                                    selectedPets = selectedPets,
                                    exerciseType = exerciseType,
                                    onFinish = { time, distance ->
                                        // 운동 결과 생성
                                        exerciseResult = ExerciseResult(
                                            time = time,
                                            distance = distance,
                                            exerciseType = exerciseType,
                                            member = uiState.userInfo!!.member,
                                            selectedPets = selectedPets
                                        )
                                        currentScreen = "result"
                                    },
                                    onBack = { currentScreen = "exerciseType" }
                                )
                            }
                        }

                        "result" -> {
                            exerciseResult?.let { result ->
                                WearExerciseResultScreen(
                                    result = result,
                                    onBackToMain = {
                                        selectedPets = emptyList()
                                        selectedExerciseType = null
                                        exerciseResult = null
                                        currentScreen = "main"
                                    }
                                )
                            }
                        }
                    }
                }

                else -> {
                    WearAuthScreen(viewModel = viewModel)
                }
            }
        }
    }
}