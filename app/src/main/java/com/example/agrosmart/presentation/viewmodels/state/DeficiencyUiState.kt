package com.example.agrosmart.presentation.viewmodels.state

import com.example.agrosmart.domain.models.Deficiency

sealed interface DeficiencyUiState {
    object Idle : DeficiencyUiState
    object Loading : DeficiencyUiState
    data class Success(val deficiencies: List<Deficiency>) : DeficiencyUiState
    data class Error(val message: String) : DeficiencyUiState
}