package com.example.agrosmart.core.utils.interfaces;

import com.example.agrosmart.domain.models.DiagnosisHistory;

import java.util.List;

public interface DiagnosisCallback{
    void onDiagnosisLoaded(List<DiagnosisHistory> historyList);
}
