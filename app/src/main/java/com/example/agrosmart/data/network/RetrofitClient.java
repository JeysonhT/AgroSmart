package com.example.agrosmart.data.network;

import com.example.agrosmart.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String URL_BASE = BuildConfig.API_URL;

    private static Retrofit retrofit = null;

    //aqui se crea el cliente http tomando como base de sus operaciones la interfaz que tiene los metodos
    // GET, POST, PUT, DELETE
    // la interfaz sera recommendationService
    public static RecommendationService recomendationService() {
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(RecommendationService.class);
    }
}
