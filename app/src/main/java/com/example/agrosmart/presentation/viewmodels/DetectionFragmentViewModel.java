package com.example.agrosmart.presentation.viewmodels;

import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.data.repository.impl.RecommendationServiceImpl;
import com.example.agrosmart.domain.models.Deficiency;
import com.example.agrosmart.domain.models.Respuesta;
import com.example.agrosmart.domain.usecase.ObtenerRecomendacionUseCase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;
import android.os.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetectionFragmentViewModel extends ViewModel {
    private final MutableLiveData<FirebaseUser> userAuth = new MutableLiveData<>();

    private final MutableLiveData<List<Deficiency>> deficiencyList = new MutableLiveData<>();

    //metodos y variables para la creacion y devolucion de recomendaciones
    private final MutableLiveData<Respuesta> recommendationResponse = new MutableLiveData<>();

    private final ObtenerRecomendacionUseCase usecase;

    public DetectionFragmentViewModel() {
        this.usecase = new ObtenerRecomendacionUseCase(new RecommendationServiceImpl());
    }

    public LiveData<Respuesta> getRecommendationResponse(){
        return recommendationResponse;
    }

    public LiveData<FirebaseUser> getUser(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userAuth.setValue(firebaseUser);

        return userAuth;
    }

    //metodo que consume el caso de uso de generacion de detecciones
    public void obtenerRecomendacion(String problema){

        final String pregunta = "Comportate como un agronomo profesional y genera recomendaciones para el siguiente problema\n" +
                problema + "\n" +
                "puedes recomendar fertilizantes organicos y no organicos, no excedas las 300 palabras";

        Handler handler = new Handler(Looper.getMainLooper());
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                usecase.ejecutar(pregunta, new Callback<Respuesta>() {
                    @Override
                    public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                        if(response.isSuccessful() && response.body() != null){
                            handler.post(()->recommendationResponse.postValue(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(Call<Respuesta> call, Throwable t) {
                        handler.post(()-> recommendationResponse.postValue(new Respuesta("error: " + t.getMessage())));
                    }
                });
            }
        });

        thread.start();
    }

    //metodo para obtener la info de deficiencias pendiente

    public void refreshData(){
        getUser();
    }



}
