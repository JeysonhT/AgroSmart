package com.example.agrosmart.data.local.mappers

import com.example.agrosmart.core.utils.classes.ImageEncoder
import com.example.agrosmart.data.local.dto.FertilizerDTO
import com.example.agrosmart.domain.models.Fertilizer

object FertilizerMapper {
    @JvmStatic
    fun toModel(dto: FertilizerDTO): Fertilizer {
        val fertilizer = Fertilizer()
        fertilizer.imageResource = ImageEncoder.decoderBase64(dto.imageFertilizers)
        fertilizer.name = dto.name
        fertilizer.applicationMethod = dto.applicationMethod
        fertilizer.recommendedDose = dto.recommendedDose
        fertilizer.description = dto.description
        fertilizer.supplier = dto.supplier
        fertilizer.type = dto.type
        return fertilizer
    }
}
