package com.example.agrosmart.data.local.mappers

import com.example.agrosmart.core.utils.classes.ImageEncoder
import com.example.agrosmart.data.local.dto.FertilizerDTO
import com.example.agrosmart.domain.models.Fertilizer
import com.google.firebase.firestore.DocumentSnapshot

object FertilizerMapper {

    @JvmStatic
    fun toModel(dto: FertilizerDTO, id: String? = null): Fertilizer {
        return Fertilizer(
            id = id ?: java.util.UUID.randomUUID().toString(),
            name = dto.name,
            imageResource = ImageEncoder.decoderBase64(dto.imageFertilizers),
            applicationMethod = dto.applicationMethod,
            recommendedDose = dto.recommendedDose,
            description = dto.description,
            supplier = dto.supplier,
            type = dto.type
        )
    }

    @JvmStatic
    fun fromDocument(doc: DocumentSnapshot): Fertilizer? {
        val data = doc.data ?: return null

        val name = doc.getString("name")
            ?: data["name"]?.toString()
            ?: return null

        val imageBase64 = doc.getString("imageFertilizers")
            ?: doc.getString("image")
            ?: doc.getString("imageUrl")
            ?: data["imageFertilizers"]?.toString()
            ?: ""

        val applicationMethod = doc.getString("applicationMethod")
            ?: doc.getString("application_method")
            ?: data["applicationMethod"]?.toString()
            ?: ""

        val recommendedDose = doc.getString("recommendedDose")
            ?: doc.getString("recommended_dose")
            ?: data["recommendedDose"]?.toString()
            ?: ""

        val description = doc.getString("description")
            ?: data["description"]?.toString()
            ?: ""

        val supplier = doc.getString("supplier")
            ?: doc.getString("provider")
            ?: data["supplier"]?.toString()
            ?: ""

        val type = doc.getString("type")
            ?: data["type"]?.toString()
            ?: ""

        val dto = FertilizerDTO(
            name = name,
            imageFertilizers = imageBase64,
            applicationMethod = applicationMethod,
            recommendedDose = recommendedDose,
            description = description,
            supplier = supplier,
            type = type
        )

        return toModel(dto, doc.id)
    }
}
