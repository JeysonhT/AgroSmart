package com.example.agrosmart.domain.repository;

import com.example.agrosmart.domain.models.Respuesta;

import retrofit2.Callback;

// este repositorio actua como ejecutador de la peticion de crear recomencadciones, haciendo uso del cliente
// http retrofit en sus implementaciones
public interface RecomendationRepository {
    void obtenerRecomendacion(String pregunta, Callback<Respuesta> callback);
}

