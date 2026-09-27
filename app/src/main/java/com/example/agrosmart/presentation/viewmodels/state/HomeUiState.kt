package com.example.agrosmart.presentation.viewmodels.state

import com.example.agrosmart.domain.designModels.CropCarouselData

sealed interface HomeUiState {
    object Idle : HomeUiState
    object Loading : HomeUiState
    data class Success(val crops: List<CropCarouselData>) : HomeUiState
    data class Error(val message: String) : HomeUiState
}