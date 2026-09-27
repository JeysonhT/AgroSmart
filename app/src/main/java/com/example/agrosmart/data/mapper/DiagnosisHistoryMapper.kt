package com.example.agrosmart.data.mapper

import com.example.agrosmart.data.local.room.entity.DiagnosisHistoryEntity
import com.example.agrosmart.domain.models.Crop
import com.example.agrosmart.domain.models.DiagnosisHistory

fun DiagnosisHistoryEntity.toDomain(): DiagnosisHistory {
    return DiagnosisHistory(
        _id = id,
        crop = Crop(
            cropName = cropName,
            description = cropDescription,
            harvestTime = cropHarvestTime,
            type = cropType
        ),
        diagnosisDate = diagnosisDate,
        deficiency = deficiency,
        image = image,
        recommendation = recommendation,
        lastUpdate = lastUpdate
    )
}

fun DiagnosisHistory.toEntity(): DiagnosisHistoryEntity {
    return DiagnosisHistoryEntity(
        id = _id,
        cropName = crop?.cropName ?: "",
        cropDescription = crop?.description ?: "",
        cropHarvestTime = crop?.harvestTime ?: "",
        cropType = crop?.type ?: "",
        diagnosisDate = diagnosisDate,
        deficiency = deficiency,
        image = image,
        recommendation = recommendation,
        lastUpdate = lastUpdate
    )
}
