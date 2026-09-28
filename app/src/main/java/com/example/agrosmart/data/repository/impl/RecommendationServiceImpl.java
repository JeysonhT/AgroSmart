package com.example.agrosmart.data.repository.impl;

import com.example.agrosmart.data.network.RecommendationService;
import com.example.agrosmart.data.network.RetrofitClient;
import com.example.agrosmart.data.network.dto.PreguntaRequest;
import com.example.agrosmart.data.network.dto.RespuestaResponse;
import com.example.agrosmart.domain.models.Respuesta;
import com.example.agrosmart.domain.repository.RecommendationRepository;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;

// implementacion del repositorio de recomendaciones
public class RecommendationServiceImpl implements RecommendationRepository {

    private static final String TAG = "RECOMMENDATION_SERVICE";
    // inicialización de lo que será el cliente http que se encargara de procesar nuestra petición
    // de generar recomendaciones al backend
    RecommendationService api;

    private final ExecutorService networkExecutor = Executors.newCachedThreadPool();

    @Inject
    public RecommendationServiceImpl(RecommendationService api) {
        this.api = api;
    }

    public RecommendationServiceImpl() {
        this.api = RetrofitClient.recommendationService();
    }

    // la respuesta de la api de gemini se obtendra a traves de los callbacks debido a la naturaleza
    // de las peticiones en la red, en otras palabras, espera a que se obtenga una respuesta para mostrar
    // un resultado

    @Override
    public CompletableFuture<Respuesta> obtenerRecomendacion(String pregunta) {

        Call<RespuestaResponse> call = api.enviarPregunta(new PreguntaRequest(pregunta));

         return CompletableFuture.supplyAsync(() -> {
            try{
                assert call != null;
                Response<RespuestaResponse> response = call.execute();
                if(response.isSuccessful() && response.body() != null){
                    return new Respuesta(response.body().getResponse());
                } else {
                    throw  new RuntimeException("Error en la respuesta de la api, intente mas tarde:\n" +
                            " " + response.code());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }, networkExecutor);
    }
}
