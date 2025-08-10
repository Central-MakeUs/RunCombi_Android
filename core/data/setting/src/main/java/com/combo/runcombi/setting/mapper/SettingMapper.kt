package com.combo.runcombi.setting.mapper

import com.combo.runcombi.network.model.response.CheckVersionResponse
import com.combo.runcombi.network.model.response.DeleteDataResponse
import com.combo.runcombi.setting.model.DeleteData

fun DeleteDataResponse.toDomainModel(): DeleteData {
    return with(result) {
        DeleteData(
            resultPetName = resultPetName ?: emptyList(),
            resultRun = resultRun ?: 0,
            resultRunImage = resultRunImage ?: 0
        )
    }
}

fun CheckVersionResponse.toDomainModel(): Boolean {
    return with(result) {
        updateRequire == "Y"
    }
}


