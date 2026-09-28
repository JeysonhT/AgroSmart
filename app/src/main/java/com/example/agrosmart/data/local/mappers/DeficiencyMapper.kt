package com.example.agrosmart.data.local.mappers

import com.example.agrosmart.core.utils.classes.ImageEncoder
import com.example.agrosmart.data.local.dto.DeficiencyDTO
import com.example.agrosmart.domain.models.Deficiency

object DeficiencyMapper {
    @JvmStatic
    fun toModel(dto: DeficiencyDTO): Deficiency {
        val deficiency = Deficiency()
        deficiency.imageResource = ImageEncoder.decoderBase64(dto.imageDeficiencies?: "")
        deficiency.name = dto.title
        deficiency.description = dto.description
        deficiency.symptoms = dto.symptoms
        deficiency.solutions = dto.solutions
        // imageResource is not mapped
        return deficiency
    }
}
