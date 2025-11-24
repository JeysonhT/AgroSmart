package com.example.agrosmart.core.utils.interfaces;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;

import com.example.agrosmart.data.local.dto.MMLResultDTO;
import com.example.agrosmart.domain.models.DetectionResult;
import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.models.MMLStats;
import com.example.agrosmart.domain.models.Respuesta;

import org.tensorflow.lite.support.image.TensorImage;

import java.util.List;
import java.util.function.Consumer;

public interface IDetectionViewModel {
    LiveData<Respuesta> getRecommendationResponse();

    LiveData<DiagnosisHistory> getLastDiagnosis() ;

    void obtenerRecomendacion(String problema);

    void saveDiagnosis(String diagnosis, byte[] image, Consumer<DiagnosisHistory> onSave);

    LiveData<List<DiagnosisHistory>> getHistory();

    void gethistoriesFromUseCase();

    void addNewHistory(DiagnosisHistory newHistory);

    void deleteHistory(String _id);

    void saveRecommendationInDiagnosis(String _id, String value);

    void refreshData();
    void cleanRecommendation();

    void sendStatsToFirebase(MMLStats stats);

    void sendResulToFirebase(DetectionResult result);

    TensorImage bitmapToTensor(Bitmap bitmap);

    MMLResultDTO processDetection(TensorImage image, Context context);
}
