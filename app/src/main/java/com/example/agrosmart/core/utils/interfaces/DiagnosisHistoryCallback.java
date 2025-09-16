package com.example.agrosmart.core.utils.interfaces;

import com.example.agrosmart.domain.models.DiagnosisHistory;

import java.util.List;

public interface DiagnosisHistoryCallback {
    void onLoaded(List<DiagnosisHistory> history);
    void onError(Exception e);
}
