package com.example.agrosmart.domain.repository

import com.example.agrosmart.domain.models.Respuesta

interface RecommendationRepository {
    suspend fun obtenerRecomendacion(pregunta: String): Respuesta?
}
