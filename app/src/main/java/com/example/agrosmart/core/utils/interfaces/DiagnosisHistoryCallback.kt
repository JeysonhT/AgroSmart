package com.example.agrosmart.core.utils.interfaces

import com.example.agrosmart.domain.models.DiagnosisHistory

interface DiagnosisHistoryCallback {
    fun onLoaded(history: List<DiagnosisHistory>?)
    fun onError(e: Exception?)
}
