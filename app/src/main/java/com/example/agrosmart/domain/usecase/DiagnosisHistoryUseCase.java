package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.core.utils.interfaces.DiagnosisCallback;
import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback;
import com.example.agrosmart.data.local.HistoryLocalService;
import com.example.agrosmart.data.repository.impl.DiagnosisHistoryLocalRepositoryImpl;
import com.example.agrosmart.domain.models.DiagnosisHistory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DiagnosisHistoryUseCase {
    private HistoryLocalService historyLocalService;

    public DiagnosisHistoryUseCase(){
        historyLocalService = new HistoryLocalService(new DiagnosisHistoryLocalRepositoryImpl());
    }

    public CompletableFuture<List<DiagnosisHistory>> getHistories(){
        return historyLocalService.getHistories();
    }

    public void saveDiagnosis(DiagnosisHistory history, DiagnosisHistoryCallback callback){
        historyLocalService.saveDiagnosis(history, callback);
    }

    public void deleteDiagnosis(String _id){
        historyLocalService.deleteDiagnosis(_id);
    }

    public void updateDiagnosis(String _id, String value){
        if(_id.isBlank()){
            throw new IllegalArgumentException("El id no puede estar vacio");
        } else {
            historyLocalService.updateDiagnosis(_id, value);
        }
    }
}

