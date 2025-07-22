package com.example.agrosmart.data.repository.impl;

import com.example.agrosmart.data.network.RecommendationService;
import com.example.agrosmart.data.network.RetrofitClient;
import com.example.agrosmart.data.network.dto.PreguntaRequest;
import com.example.agrosmart.data.network.dto.RespuestaResponse;
import com.example.agrosmart.domain.models.Respuesta;
import com.example.agrosmart.domain.repository.RecomendationRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// implementacion del repositorio de recomendaciones
public class RecommendationServiceImpl implements RecomendationRepository {

    // inicialización de lo que sera el cliente http que se encargara de procesar nuestra peticion
    // de generar recomendaciones al backend
    RecommendationService api;

    public RecommendationServiceImpl() {
        this.api = RetrofitClient.recomendationService();
    }

    // la respuesta de la api de gemini se otendra a travez de los callbacks debido a la naturaleza
    // de las peticiones en la red, en otras palabras, espera a que se obtenga una respuesta para mostrar
    // un resultado
    @Override
    public void obtenerRecomendacion(String pregunta, Callback<Respuesta> callback) {
        // aqui se realiza la peticion y obtendremos un callback para procesar lo que pasa
        Call<RespuestaResponse> call = api.enviarPregunta(new PreguntaRequest(pregunta));

        call.enqueue(new Callback<RespuestaResponse>() {
            @Override
            public void onResponse(Call<RespuestaResponse> call, Response<RespuestaResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    callback.onResponse(null, Response.success(new Respuesta(response
                                    .body().
                                    getRespuesta())));
                } else {
                    callback.onFailure(null, new Throwable("error en la resuesta de la api"));
                }
            }

            @Override
            public void onFailure(Call<RespuestaResponse> call, Throwable t) {
                callback.onFailure(null, t);
            }
        });
    }
}
