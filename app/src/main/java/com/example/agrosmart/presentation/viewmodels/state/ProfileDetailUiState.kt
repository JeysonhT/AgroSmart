package com.example.agrosmart.presentation.viewmodels.state

import com.example.agrosmart.domain.models.UserDetails

sealed interface ProfileDetailUiState {
    object Idle : ProfileDetailUiState
    object Loading : ProfileDetailUiState
    data class Success(val userDetails: UserDetails?) : ProfileDetailUiState
    data class Error(val message: String) : ProfileDetailUiState
}