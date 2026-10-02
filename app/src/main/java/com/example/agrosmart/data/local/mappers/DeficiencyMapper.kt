package com.example.agrosmart.data.local.mappers

import com.example.agrosmart.core.utils.classes.ImageEncoder
import com.example.agrosmart.data.local.dto.DeficiencyDTO
import com.example.agrosmart.domain.models.Deficiency
import com.google.firebase.firestore.DocumentSnapshot
import java.util.UUID

object DeficiencyMapper {

    @JvmStatic
    @JvmOverloads
    fun toModel(dto: DeficiencyDTO, id: String? = null): Deficiency {
        val deficiency = Deficiency()
        deficiency.set_id(id ?: dto.id ?: UUID.randomUUID().toString())
        deficiency.name = dto.title
        deficiency.imageResource = ImageEncoder.decoderBase64(dto.imageDeficiencies ?: "")
        deficiency.description = dto.description
        deficiency.symptoms = dto.symptoms
        deficiency.solutions = dto.solutions
        return deficiency
    }

    @JvmStatic
    fun fromDocument(doc: DocumentSnapshot): Deficiency? {
        val data = doc.data ?: return null

        val title = doc.getString("title")
            ?: doc.getString("name")
            ?: data["title"]?.toString()
            ?: data["name"]?.toString()
            ?: return null

        val imageBase64 = doc.getString("imageDeficiencies")
            ?: doc.getString("image")
            ?: doc.getString("imageUrl")
            ?: data["imageDeficiencies"]?.toString()
            ?: data["image"]?.toString()
            ?: ""

        val description = doc.getString("description")
            ?: data["description"]?.toString()
            ?: ""

        val symptoms = doc.getString("symptoms")
            ?: doc.getString("symptom")
            ?: data["symptoms"]?.toString()
            ?: ""

        val solutions = doc.getString("solutions")
            ?: doc.getString("solution")
            ?: data["solutions"]?.toString()
            ?: ""

        val dto = DeficiencyDTO(
            id = doc.id,
            imageDeficiencies = imageBase64,
            title = title,
            description = description,
            symptoms = symptoms,
            solutions = solutions
        )

        return toModel(dto, doc.id)
    }
}
