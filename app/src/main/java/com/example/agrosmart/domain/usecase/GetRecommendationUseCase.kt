package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.models.Respuesta
import com.example.agrosmart.domain.repository.RecommendationRepository
import javax.inject.Inject

class GetRecommendationUseCase @Inject constructor(
    private val repository: RecommendationRepository
) {

    suspend operator fun invoke(pregunta: String): Respuesta? {
        return repository.obtenerRecomendacion(pregunta)
    }

    suspend fun ejecutar(pregunta: String): Respuesta? {
        return invoke(pregunta)
    }
}
