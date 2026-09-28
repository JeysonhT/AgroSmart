package com.example.agrosmart.data.network.retrofitservices

import com.example.agrosmart.data.network.dto.PreguntaRequest
import com.example.agrosmart.data.network.dto.RespuestaResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RecommendationService {
    @POST("api/recommendation/getRecommendation")
    suspend fun enviarPregunta(@Body pregunta: PreguntaRequest): Response<RespuestaResponse>
}