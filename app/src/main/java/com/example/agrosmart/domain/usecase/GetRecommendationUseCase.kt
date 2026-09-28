package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.models.Respuesta
import com.example.agrosmart.domain.repository.RecommendationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

class GetRecommendationUseCase @Inject constructor(
    private val repository: RecommendationRepository
) {
    suspend operator fun invoke(pregunta: String): Result<Respuesta> = withContext(Dispatchers.IO) {
        runCatching {
            repository.obtenerRecomendacion(pregunta).get()
        }
    }

    // Java backward-compatibility
    fun ejecutar(pregunta: String): CompletableFuture<Respuesta> {
        return repository.obtenerRecomendacion(pregunta)
    }
}
