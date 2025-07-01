package com.combo.runcombi.main

import androidx.lifecycle.ViewModel
import com.combo.runcombi.core.navigation.NavigationEventHandler
import com.combo.runcombi.core.navigation.di.AuthNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @AuthNavigation navigationEventHandler: NavigationEventHandler,
) : ViewModel() {
    val navigationEvent = navigationEventHandler.routeToNavigate
}
