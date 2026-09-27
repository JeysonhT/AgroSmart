package com.example.agrosmart.presentation.viewmodels.state

import com.example.agrosmart.domain.models.User
import com.example.agrosmart.domain.models.UserDetails

sealed interface ProfileUiState {
    object Idle : ProfileUiState
    object Loading : ProfileUiState
    data class Success(
        val user: User?,
        val userDetails: UserDetails? = null
    ) : ProfileUiState
    data class Error(val message: String) : ProfileUiState
}