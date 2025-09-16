package com.example.agrosmart.domain.repository;

import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback;
import com.example.agrosmart.domain.models.DiagnosisHistory;

import java.util.List;

public interface DiagnosisHistoryRepository {
    List<DiagnosisHistory> getDiagnosisHistories();
    void saveDiagnosis(DiagnosisHistory history);
    void deleteEmptyDiagnosis();
    void deleteDiagnosis(String _id);
}
