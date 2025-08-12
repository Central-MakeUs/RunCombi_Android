package com.combo.runcombi.signup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.combo.runcombi.analytics.AnalyticsHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CompleteViewModel @Inject constructor(
    val analyticsHelper: AnalyticsHelper,
) : ViewModel()
