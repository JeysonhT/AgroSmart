package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.domain.models.Respuesta;
import com.example.agrosmart.domain.repository.RecomendationRepository;

import retrofit2.Callback;

public class ObtenerRecomendacionUseCase {

    private RecomendationRepository repository;

    public ObtenerRecomendacionUseCase(RecomendationRepository _repository){
        this.repository = _repository;
    }

    public void ejecutar(String pregunta, Callback<Respuesta> callback) {
        repository.obtenerRecomendacion(pregunta, callback);
    }
}
