package com.combo.runcombi.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.common.DomainResult
import com.combo.runcombi.core.navigation.NavigationEventHandler
import com.combo.runcombi.core.navigation.di.AuthNavigation
import com.combo.runcombi.setting.repository.SettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @AuthNavigation navigationEventHandler: NavigationEventHandler,
    private val settingRepository: SettingRepository,
) : ViewModel() {
    val navigationEvent = navigationEventHandler.routeToNavigate

    private val _showForceUpdateDialog = MutableStateFlow(false)
    val showForceUpdateDialog: StateFlow<Boolean> = _showForceUpdateDialog.asStateFlow()

    fun checkAppVersion(currentVersion: String) {
        viewModelScope.launch {
            try {
                val result = settingRepository.checkVersion(currentVersion)
                
                if (result is DomainResult.Success) {
                    if (result.data) {
                        _showForceUpdateDialog.value = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}
