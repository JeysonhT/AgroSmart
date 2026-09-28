package com.example.agrosmart.data.repository.impl

import android.util.Log
import com.example.agrosmart.data.network.retrofitservices.RecommendationService
import com.example.agrosmart.data.network.dto.PreguntaRequest
import com.example.agrosmart.di.IoDispatcher
import com.example.agrosmart.domain.models.Respuesta
import com.example.agrosmart.domain.repository.RecommendationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationServiceImpl @Inject constructor(
    private val api: RecommendationService,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RecommendationRepository {

    override suspend fun obtenerRecomendacion(pregunta: String): Respuesta? = withContext(ioDispatcher) {
        try {
            val response = api.enviarPregunta(PreguntaRequest(pregunta))

            if (response.isSuccessful && response.body() != null) {
                Respuesta(response.body()!!.response)
            } else {
                val errorMsg = "Error en la respuesta de la API (${response.code()}): ${response.message()}"
                Log.e(TAG, errorMsg)
                throw RuntimeException(errorMsg)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener recomendación desde la API", e)
            throw e
        }
    }

    companion object {
        private const val TAG = "RECOMMENDATION_SERVICE"
    }
}
