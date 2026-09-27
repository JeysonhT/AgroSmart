package com.example.agrosmart.presentation.viewmodels.state

import com.example.agrosmart.domain.models.Fertilizer

sealed interface FertilizerUiState {
    object Idle : FertilizerUiState
    object Loading : FertilizerUiState
    data class Success(val fertilizers: List<Fertilizer>) : FertilizerUiState
    data class Error(val message: String) : FertilizerUiState
}