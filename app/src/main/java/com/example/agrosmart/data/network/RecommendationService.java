package com.example.agrosmart.data.network;

import com.example.agrosmart.data.network.dto.PreguntaRequest;
import com.example.agrosmart.data.network.dto.RespuestaResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

// esta interfaz poseera los metodos del cliente http retrofit
public interface RecommendationService {

    @POST("api/Recommendation")
    Call<RespuestaResponse> enviarPregunta(@Body PreguntaRequest pregunta);
}
