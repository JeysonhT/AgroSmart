package com.example.agrosmart.presentation.viewmodels.state

import com.example.agrosmart.domain.models.DiagnosisHistory

sealed interface DetectionUiState {
    object Idle : DetectionUiState
    object Loading : DetectionUiState
    data class Success(
        val diagnosis: DiagnosisHistory? = null,
        val recommendation: String = "",
        val histories: List<DiagnosisHistory> = emptyList()
    ) : DetectionUiState
    data class Error(val message: String) : DetectionUiState
}