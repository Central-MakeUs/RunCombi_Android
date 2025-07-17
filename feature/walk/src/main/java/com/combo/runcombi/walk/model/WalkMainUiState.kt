package com.combo.runcombi.walk.model

import com.combo.runcombi.domain.user.model.Member
import com.combo.runcombi.domain.user.model.Pet
import com.google.android.gms.maps.model.LatLng

data class PetUiModel(
    val pet: Pet,
    val isSelected: Boolean = false,
    val originIndex: Int,
    val selectedOrder: Int? = null, // 선택 순서(없으면 미선택)
)

data class WalkMainUiState(
    val myLocation: LatLng? = null,
    val member: Member? = null,
    val petUiList: List<PetUiModel> = emptyList(),
    val address: String = "",
    val isLoading: Boolean = false,
)