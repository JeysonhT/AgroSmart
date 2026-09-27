package com.example.agrosmart.data.local.mappers

import com.example.agrosmart.core.utils.classes.ImageEncoder
import com.example.agrosmart.data.local.dto.FertilizerDTO
import com.example.agrosmart.domain.models.Fertilizer

object FertilizerMapper {
    @JvmStatic
    fun toModel(dto: FertilizerDTO): Fertilizer {
        val fertilizer = Fertilizer()
        fertilizer.setImageResource(ImageEncoder.decoderBase64(dto.getImageFertilizers()))
        fertilizer.setName(dto.getName())
        fertilizer.setApplicationMethod(dto.getApplicationMethod())
        fertilizer.setRecommendedDose(dto.getRecommendedDose())
        fertilizer.setDescription(dto.getDescription())
        fertilizer.setSupplier(dto.getSupplier())
        fertilizer.setType(dto.getType())
        return fertilizer
    }
}
