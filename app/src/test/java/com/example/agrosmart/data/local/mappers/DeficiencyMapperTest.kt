package com.example.agrosmart.data.local.mappers

import com.example.agrosmart.data.local.dto.DeficiencyDTO
import com.google.firebase.firestore.DocumentSnapshot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class DeficiencyMapperTest {

    @Test
    fun toModel_mapsAllFieldsCorrectly() {
        val dto = DeficiencyDTO(
            id = "test-id",
            imageDeficiencies = "",
            title = "Deficiencia de Potasio",
            description = "Bordes quemados",
            symptoms = "Necrosis marginal",
            solutions = "Aplicar sulfato de potasio"
        )

        val model = DeficiencyMapper.toModel(dto)

        assertEquals("test-id", model.get_id())
        assertEquals("Deficiencia de Potasio", model.name)
        assertEquals("Bordes quemados", model.description)
        assertEquals("Necrosis marginal", model.symptoms)
        assertEquals("Aplicar sulfato de potasio", model.solutions)
    }

    @Test
    fun fromDocument_mapsSnapshotCorrectly() {
        val snapshot = mock(DocumentSnapshot::class.java)
        val dataMap = mapOf<String, Any>(
            "title" to "Deficiencia de Calcio",
            "description" to "Pudrición apical",
            "symptoms" to "Deformación de hojas jóvenes",
            "solutions" to "Aplicar nitrato de calcio"
        )

        `when`(snapshot.data).thenReturn(dataMap)
        `when`(snapshot.id).thenReturn("doc-123")
        `when`(snapshot.getString("title")).thenReturn("Deficiencia de Calcio")
        `when`(snapshot.getString("imageDeficiencies")).thenReturn(null)
        `when`(snapshot.getString("image")).thenReturn(null)
        `when`(snapshot.getString("imageUrl")).thenReturn(null)
        `when`(snapshot.getString("description")).thenReturn("Pudrición apical")
        `when`(snapshot.getString("symptoms")).thenReturn("Deformación de hojas jóvenes")
        `when`(snapshot.getString("solutions")).thenReturn("Aplicar nitrato de calcio")

        val model = DeficiencyMapper.fromDocument(snapshot)

        assertNotNull(model)
        assertEquals("doc-123", model?.get_id())
        assertEquals("Deficiencia de Calcio", model?.name)
        assertEquals("Pudrición apical", model?.description)
        assertEquals("Deformación de hojas jóvenes", model?.symptoms)
        assertEquals("Aplicar nitrato de calcio", model?.solutions)
    }

    @Test
    fun fromDocument_returnsNullWhenNameAndTitleAreMissing() {
        val snapshot = mock(DocumentSnapshot::class.java)
        val dataMap = mapOf<String, Any>(
            "description" to "Sin título"
        )

        `when`(snapshot.data).thenReturn(dataMap)
        `when`(snapshot.getString("title")).thenReturn(null)
        `when`(snapshot.getString("name")).thenReturn(null)

        val model = DeficiencyMapper.fromDocument(snapshot)

        assertNull(model)
    }

    @Test
    fun fromDocument_returnsNullWhenDataIsNull() {
        val snapshot = mock(DocumentSnapshot::class.java)
        `when`(snapshot.data).thenReturn(null)

        val model = DeficiencyMapper.fromDocument(snapshot)

        assertNull(model)
    }
}
