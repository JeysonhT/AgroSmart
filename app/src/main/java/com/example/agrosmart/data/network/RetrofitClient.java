package com.example.agrosmart.data.network;

import android.os.Build;

import com.example.agrosmart.BuildConfig;
import com.example.agrosmart.core.utils.classes.UnsafeOkhttpClient;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
            OkHttpClient okHttpClient;
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.N){
                okHttpClient = UnsafeOkhttpClient.getUnsafeOkHttpClient();
            } else {
                okHttpClient = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build();
            }

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_BASE)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(RecommendationService.class);
    }
}
