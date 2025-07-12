package com.combo.runcombi.walk.model

import com.combo.runcombi.domain.user.model.User
import com.combo.runcombi.pet.model.Pet
import com.google.android.gms.maps.model.LatLng

data class PetUiModel(
    val pet: Pet,
    val isSelected: Boolean = false,
    val originIndex: Int,
    val selectedOrder: Int? = null // 선택 순서(없으면 미선택)
)

data class WalkMainUiState(
    val myLocation: LatLng? = null,
    val user: User? = null,
    val petUiList: List<PetUiModel> = emptyList(),
    val address: String = "",
    val isLoading: Boolean = false,
)