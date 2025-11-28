package com.example.agrosmart.presentation.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback;
import com.example.agrosmart.core.utils.interfaces.IDetectionViewModel;
import com.example.agrosmart.data.local.dto.MMLResultDTO;
import com.example.agrosmart.data.network.MMLStatsService;
import com.example.agrosmart.data.repository.impl.MMLStatsRepositoryImpl;
import com.example.agrosmart.data.repository.impl.RecommendationServiceImpl;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.models.DetectionResult;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.models.MMLStats;
import com.example.agrosmart.domain.models.Respuesta;
import com.example.agrosmart.domain.usecase.CropsUseCase;
import com.example.agrosmart.domain.usecase.DetectionResultUseCase;
import com.example.agrosmart.domain.usecase.DetectionUseCase;
import com.example.agrosmart.domain.usecase.DiagnosisHistoryUseCase;
import com.example.agrosmart.domain.usecase.GetRecommendationUseCase;
import com.example.agrosmart.domain.usecase.MMLStatsUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.support.image.TensorImage;

public class DetectionFragmentViewModel extends ViewModel implements IDetectionViewModel {

    private final String TAG = "DETECTION_FRAGMENT_VIEWMODDEL";

    private final MutableLiveData<List<DiagnosisHistory>> histories = new MutableLiveData<>();
    private final MutableLiveData<DiagnosisHistory> lastDiagnosis = new MutableLiveData<>();

    //metodos y variables para la creacion y devolucion de recomendaciones
    private final MutableLiveData<Respuesta> recommendationResponse = new MutableLiveData<>();

    private final GetRecommendationUseCase usecase;

    private DiagnosisHistoryUseCase diagnosisUseCase;

    private MMLStatsUseCase mmlUseCase;
    private DetectionResultUseCase drUseCase;
    private DetectionUseCase detectionUseCase;

    public DetectionFragmentViewModel() {
        this.usecase = new GetRecommendationUseCase(new RecommendationServiceImpl());
        this.diagnosisUseCase = new DiagnosisHistoryUseCase();
        this.cropsCase = new CropsUseCase();

        this.mmlUseCase = new MMLStatsUseCase(new MMLStatsService(new MMLStatsRepositoryImpl()));
        this.drUseCase = new DetectionResultUseCase();
        this.detectionUseCase = new DetectionUseCase();
    }

    private CropsUseCase cropsCase;

    public DetectionFragmentViewModel(GetRecommendationUseCase _usecase,
                                      DiagnosisHistoryUseCase _diagnosisUseCase,
                                      CropsUseCase _cropsCase){
        this.usecase = _usecase;
        this.diagnosisUseCase = _diagnosisUseCase;
        this.cropsCase = _cropsCase;
    }

    public DetectionFragmentViewModel(GetRecommendationUseCase _usecase){
        this.usecase = _usecase;
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
                "no excedas las 300 palabras, las listas crealas usando guiones y evita el uso de ateriscos para titulos y para los nombres de la soluciones, " +
                "dejalos separados de los parrafos para obtener un texto mas limpio";

        usecase.ejecutar(pregunta).thenAccept(recommendationResponse::postValue).exceptionally(error -> {
            Log.e(TAG, "Error al obtener recomendación", error);
            recommendationResponse.postValue(new Respuesta("error"));
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
            if (values != null && !values.isEmpty()) {
                lastDiagnosis.postValue(values.get(0));
            } else {
                lastDiagnosis.postValue(null); // Explicitly set to null if list is empty
            }
        }).exceptionally( error -> {
            histories.postValue(Collections.emptyList()); // Explicitly set to null on error
            lastDiagnosis.postValue(null); // Explicitly set to null on error
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

    public void saveRecommendationInDiagnosis(String _id, String value){
        diagnosisUseCase.updateDiagnosis(_id, value);
    }

    //refresca la información del usuario
    public void refreshData(){
        gethistoriesFromUseCase();
    }

    public void cleanRecommendation(){
        recommendationResponse.postValue(null);
    }

    @Override
    public void sendStatsToFirebase(MMLStats stats) {
        mmlUseCase.saveStats(stats);
    }

    @Override
    public void sendResulToFirebase(DetectionResult result) {
        drUseCase.saveResult(result);
    }

    @Override
    public TensorImage bitmapToTensor(Bitmap bitmap) {
        return detectionUseCase.bitMapToTensor(bitmap);
    }

    @Override
    public MMLResultDTO processDetection(TensorImage image, Context context) {
        return detectionUseCase.processDetection(image, context);
    }
}
