package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.core.utils.interfaces.DiagnosisCallback;
import com.example.agrosmart.data.local.HistoryLocalService;
import com.example.agrosmart.data.repository.impl.DiagnosisHistoryLocalRepositoryImpl;
import com.example.agrosmart.domain.models.DiagnosisHistory;

public class DiagnosisHistoryUseCase {
    private HistoryLocalService historyLocalService;

    public DiagnosisHistoryUseCase(){
        historyLocalService = new HistoryLocalService(new DiagnosisHistoryLocalRepositoryImpl());
    }

    public void getHistories(DiagnosisCallback callback){
        callback.onDiagnosisLoaded(historyLocalService.getHistories());
    }

    public void saveDiagnosis(DiagnosisHistory history){
        historyLocalService.saveDiagnosis(history);
    }

    public void deleteDiagnosis(String _id){
        historyLocalService.deleteDiagnosis(_id);
    }
}

