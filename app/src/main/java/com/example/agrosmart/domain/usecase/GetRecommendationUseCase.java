package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.domain.models.Respuesta;
import com.example.agrosmart.domain.repository.RecomendationRepository;

import java.util.concurrent.CompletableFuture;

import retrofit2.Callback;

public class GetRecommendationUseCase {

    private RecomendationRepository repository;

    public GetRecommendationUseCase(RecomendationRepository _repository){
        this.repository = _repository;
    }

    public CompletableFuture<Respuesta> ejecutar(String pregunta) {
        return repository.obtenerRecomendacion(pregunta);
    }
}
