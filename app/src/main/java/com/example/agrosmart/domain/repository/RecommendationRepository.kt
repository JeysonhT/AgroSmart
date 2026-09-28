package com.example.agrosmart.domain.repository

import com.example.agrosmart.domain.models.Respuesta
import java.util.concurrent.CompletableFuture

// este repositorio actúa como ejecutador de la petición de crear recomendaciones, haciendo uso del cliente
// http retrofit en sus implementaciones
interface RecommendationRepository {
    fun obtenerRecomendacion(pregunta: String?): CompletableFuture<Respuesta?>?
}

