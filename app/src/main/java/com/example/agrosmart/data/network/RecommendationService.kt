package com.example.agrosmart.data.network

import com.example.agrosmart.data.network.dto.PreguntaRequest
import com.example.agrosmart.data.network.dto.RespuestaResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

// esta interfaz poseera los metodos del cliente http retrofit
interface RecommendationService {
    @POST("api/recommendation/getRecommendation")
    fun enviarPregunta(@Body pregunta: PreguntaRequest?): Call<RespuestaResponse?>?
}
