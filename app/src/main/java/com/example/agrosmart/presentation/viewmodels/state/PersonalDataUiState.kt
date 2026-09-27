package com.example.agrosmart.presentation.viewmodels.state

import com.example.agrosmart.domain.models.DiagnosisHistory

sealed interface PersonalDataUiState {
    object Idle : PersonalDataUiState
    object Loading : PersonalDataUiState
    data class Success(
        val topCrops: List<String> = emptyList(),
        val topDeficiencies: List<String> = emptyList(),
        val diagnosisGenerated: Int = 0,
        val recommendationsSaved: Int = 0,
        val diagnosisHistory: List<DiagnosisHistory> = emptyList()
    ) : PersonalDataUiState
    data class Error(val message: String) : PersonalDataUiState
}