package com.example.agrosmart.domain.models.mappers

import com.example.agrosmart.data.local.dto.CropDTO
import com.example.agrosmart.domain.models.Crop

object CropMapper {
    // De Realm a DTO (para guardar o enviar a Firestore)
    fun toDto(crop: Crop?): CropDTO? {
        if (crop == null) return null
        return CropDTO(
                crop.cropName,
                crop.description,
                crop.harvestTime,
                crop.type
        )
    }

    // De DTO a Realm (para guardar localmente)
    @JvmStatic
    fun toEntity(dto: CropDTO?): Crop? {
        if (dto == null) return null
        val crop = Crop()
        crop.cropName = dto.cropName!!
        crop.description = dto.description!!
        crop.harvestTime = dto.content!!
        crop.type = dto.type!!
        return crop
    }
}
