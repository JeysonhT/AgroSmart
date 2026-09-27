package com.example.agrosmart.data.local.mappers

import com.example.agrosmart.core.utils.classes.ImageEncoder
import com.example.agrosmart.data.local.dto.DeficiencyDTO
import com.example.agrosmart.domain.models.Deficiency

object DeficiencyMapper {
    @JvmStatic
    fun toModel(dto: DeficiencyDTO): Deficiency {
        val deficiency = Deficiency()
        deficiency.setImageResource(ImageEncoder.decoderBase64(dto.getImageDeficiencies()))
        deficiency.setName(dto.getTitle())
        deficiency.setDescription(dto.getDescription())
        deficiency.setSymptoms(dto.getSymptoms())
        deficiency.setSolutions(dto.getSolutions())
        // imageResource is not mapped
        return deficiency
    }
}
