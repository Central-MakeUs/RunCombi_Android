package com.combo.runcombi.setting.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.combo.runcombi.setting.viewmodel.AccountDeletionViewModel

@Composable
fun UserWithdrawalSurvey(
    onBack: ()-> Unit,
    onComplete: () -> Unit,
    accountDeletionViewModel: AccountDeletionViewModel = hiltViewModel(),
) {

}