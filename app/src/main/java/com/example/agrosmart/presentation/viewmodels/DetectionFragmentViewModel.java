package com.example.agrosmart.presentation.viewmodels;

import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.R;
import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback;
import com.example.agrosmart.data.repository.impl.RecommendationServiceImpl;
import com.example.agrosmart.domain.designModels.DiagnosisHistoryListView;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.models.Respuesta;
import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.domain.usecase.DiagnosisHistoryUseCase;
import com.example.agrosmart.domain.usecase.GetRecommendationUseCase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import android.os.Handler;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetectionFragmentViewModel extends ViewModel {

    private final String TAG = "DETECTION_FRAGMENT_VIEWMODDEL";

    private final MutableLiveData<List<DiagnosisHistory>> histories = new MutableLiveData<>();
    private final MutableLiveData<DiagnosisHistory> lastDiagnosis = new MutableLiveData<>();

    //metodos y variables para la creacion y devolucion de recomendaciones
    private final MutableLiveData<Respuesta> recommendationResponse = new MutableLiveData<>();

    private final GetRecommendationUseCase usecase;

    private final DiagnosisHistoryUseCase diagnosisUseCase;

    public DetectionFragmentViewModel() {
        this.usecase = new GetRecommendationUseCase(new RecommendationServiceImpl());
        this.diagnosisUseCase = new DiagnosisHistoryUseCase();
        this.cropsCase = new CropsUseCase();
    }

    private final CropsUseCase cropsCase;

    public DetectionFragmentViewModel(GetRecommendationUseCase _usecase,
                                      DiagnosisHistoryUseCase _diagnosisUseCase,
                                      CropsUseCase _cropsCase){
        this.usecase = _usecase;
        this.diagnosisUseCase = _diagnosisUseCase;
        this.cropsCase = _cropsCase;
    }

    public LiveData<Respuesta> getRecommendationResponse(){
        return recommendationResponse;
    }

    public LiveData<DiagnosisHistory> getLastDiagnosis() {
        return lastDiagnosis; }

    //metodo que consume el caso de uso de generacion de detecciones
    public void obtenerRecomendacion(String problema){

        final String pregunta = "Comportate como un agronomo profesional y " +
                "genera recomendaciones para el siguiente problema\n" +
                problema + "\n" +
                "puedes recomendar fertilizantes organicos y no organicos, " +
                "no excedas las 300 palabras";

        usecase.ejecutar(pregunta).thenAccept(recommendationResponse::postValue).exceptionally(error -> {
            Log.e(TAG, "Error al obtener recomendación", error);
            return null;
        });
    }

    public void saveDiagnosis(String diagnosis, byte[] image, Consumer<DiagnosisHistory> onSave){
        String name = diagnosis.split(" ")[0];
        cropsCase.getCropByName(name, new CropsCallback() {
            @Override
            public void onCropsLoaded(List<Crop> crops) {
                if (crops == null || crops.isEmpty()) {
                    onError(new Exception("Crop not found"));
                    return;
                }
                Crop crop = crops.get(0);
                DiagnosisHistory diagnosisHistory = DiagnosisHistory.builder()
                        ._id(UUID.randomUUID().toString())
                        .diagnosisDate(new Date())
                        .Crop(crop)
                        .deficiency(diagnosis)
                        .image(image)
                        .recommendation("")
                        .lastUpdate(System.currentTimeMillis())
                        .build();

                onSave.accept(diagnosisHistory);

                diagnosisUseCase.saveDiagnosis(diagnosisHistory, new DiagnosisHistoryCallback() {
                    @Override
                    public void onLoaded(List<DiagnosisHistory> history) {
                        lastDiagnosis.postValue(history.get(0));
                        Log.d(TAG, "Diagnosis saved successfully");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "Error saving diagnosis", e);
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "Error getting crop by name", e);
            }
        });

    }

    public LiveData<List<DiagnosisHistory>> getHistory(){
        return histories;
    }

    public void gethistoriesFromUseCase(){
        diagnosisUseCase.getHistories().thenAccept(values -> {
            histories.postValue(values);
            lastDiagnosis.postValue(values.get(0));
        }).exceptionally( error -> {
            Log.e(TAG, Objects.requireNonNull(error.getMessage()));
            return null;
        });
    }

    public void addNewHistory(DiagnosisHistory newHistory) {
        List<DiagnosisHistory> currentList = histories.getValue();
        List<DiagnosisHistory> newList = new ArrayList<>();

        if (currentList != null) {
            newList.addAll(currentList);
        }
        newList.add(0, newHistory); // Agregar al inicio

        histories.setValue(newList);
    }

    public void deleteHistory(String _id){
        diagnosisUseCase.deleteDiagnosis(_id);
    }

    public void updateDiagnosis(String _id, String value){
        diagnosisUseCase.updateDiagnosis(_id, value);
    }

    

    //refresca la información del usuario
    public void refreshData(){
        gethistoriesFromUseCase();
    }

    public void cleanRecommendation(){
        recommendationResponse.postValue(null);
    }
}
