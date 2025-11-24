package com.example.agrosmart.presentation.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.agrosmart.core.utils.interfaces.IDetectionViewModel;
import com.example.agrosmart.data.local.dto.MMLResultDTO;
import com.example.agrosmart.domain.models.DetectionResult;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.models.MMLStats;
import com.example.agrosmart.domain.models.Respuesta;

import org.tensorflow.lite.support.image.TensorImage;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FakeDetectionViewModel extends ViewModel implements IDetectionViewModel {

    private final MutableLiveData<List<DiagnosisHistory>> histories = new MutableLiveData<>();
    private final MutableLiveData<DiagnosisHistory> lastDiagnosis = new MutableLiveData<>();

    private final MutableLiveData<Respuesta> recommendationResponse = new MutableLiveData<>();

    @Override
    public LiveData<Respuesta> getRecommendationResponse() {
        return recommendationResponse;
    }

    @Override
    public LiveData<DiagnosisHistory> getLastDiagnosis() {
        lastDiagnosis.setValue(new DiagnosisHistory());
        return lastDiagnosis;
    }

    @Override
    public void obtenerRecomendacion(String problema) {
        recommendationResponse.setValue(new Respuesta("respuesta"));
    }

    @Override
    public void saveDiagnosis(String diagnosis, byte[] image, Consumer<DiagnosisHistory> onSave) {
        lastDiagnosis.postValue(new DiagnosisHistory());
    }

    @Override
    public LiveData<List<DiagnosisHistory>> getHistory() {
        return histories;
    }

    @Override
    public void gethistoriesFromUseCase() {
        histories.setValue(Collections.emptyList());
    }

    @Override
    public void addNewHistory(DiagnosisHistory newHistory) {
        histories.setValue(Collections.emptyList());
    }

    @Override
    public void deleteHistory(String _id) {

    }

    @Override
    public void saveRecommendationInDiagnosis(String _id, String value) {

    }

    @Override
    public void refreshData() {
        gethistoriesFromUseCase();
    }

    @Override
    public void cleanRecommendation() {
        recommendationResponse.postValue(null);
    }

    @Override
    public void sendStatsToFirebase(MMLStats stats) {

    }

    @Override
    public void sendResulToFirebase(DetectionResult result) {

    }

    @Override
    public TensorImage bitmapToTensor(Bitmap bitmap) {
        return new TensorImage();
    }

    @Override
    public MMLResultDTO processDetection(TensorImage image, Context context) {
        return new MMLResultDTO("result",100L, 2048L, new float[]{1.2f});
    }
}
