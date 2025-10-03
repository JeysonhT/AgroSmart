package com.example.agrosmart.domain.repository;

import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback;
import com.example.agrosmart.domain.models.DiagnosisHistory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DiagnosisHistoryRepository {
    CompletableFuture<List<DiagnosisHistory>> getDiagnosisHistories();
    DiagnosisHistory getLastDiagnosis();
    void saveDiagnosis(DiagnosisHistory history, DiagnosisHistoryCallback callback);
    void updateDiagnosis(String _id, String param, String value);
    void deleteDiagnosis(String _id);

}
