package com.combo.runcombi.setting.viewmodel

import androidx.lifecycle.ViewModel
import com.combo.runcombi.setting.model.AddPetData
import com.combo.runcombi.setting.model.PetInfoData
import com.combo.runcombi.setting.model.PetProfileData
import com.combo.runcombi.setting.model.PetStyleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddPetViewModel : ViewModel() {
    private val _addPetData = MutableStateFlow(AddPetData())
    val addPetData: StateFlow<AddPetData> = _addPetData.asStateFlow()

    fun setPetProfile(data: PetProfileData) {
        _addPetData.value = _addPetData.value.copy(profileData = data)
    }

    fun setPetInfo(data: PetInfoData) {
        _addPetData.value = _addPetData.value.copy(infoData = data)
    }

    fun setPetStyle(data: PetStyleData) {
        _addPetData.value = _addPetData.value.copy(styleData = data)
    }

    fun getAddPetData(): AddPetData = _addPetData.value

    fun clearPetProfile() {
        _addPetData.value = _addPetData.value.copy(profileData = PetProfileData())
    }

    fun clearPetInfo() {
        _addPetData.value = _addPetData.value.copy(infoData = PetInfoData())
    }

    fun clearPetStyle() {
        _addPetData.value = _addPetData.value.copy(styleData = PetStyleData())
    }
} 