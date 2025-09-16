package com.example.agrosmart.data.local;

import com.example.agrosmart.domain.models.DiagnosisHistory;
import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository;

import java.util.List;

public class HistoryLocalService {
    private final DiagnosisHistoryRepository repository;

    public HistoryLocalService(DiagnosisHistoryRepository repository) {
        this.repository = repository;
    }

    public List<DiagnosisHistory> getHistories(){
        return repository.getDiagnosisHistories();
    }

    public void saveDiagnosis(DiagnosisHistory history){
        repository.saveDiagnosis(history);
    }

    public void deleteDiagnosis(String _id){
        repository.deleteDiagnosis(_id);
    }
}
