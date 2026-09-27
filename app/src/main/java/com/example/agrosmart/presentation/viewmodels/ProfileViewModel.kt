package com.example.agrosmart.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrosmart.data.repository.impl.UserDtlimpl
import com.example.agrosmart.domain.models.User
import com.example.agrosmart.domain.repository.UserDtlRepository
import com.example.agrosmart.presentation.viewmodels.state.ProfileUiState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: UserDtlRepository
) : ViewModel() {

    // Constructor secundario para compatibilidad sin Hilt
    constructor() : this(UserDtlimpl())

    private val tag = "PROFILE_VIEW_MODEL"

    // Modern StateFlow UI State (UDF) - Single Source of Truth
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Idle)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val userAuth = if (firebaseUser != null) {
            User(
                firebaseUser.displayName,
                firebaseUser.email,
                firebaseUser.photoUrl
            )
        } else {
            null
        }
        val currentDetails = (_uiState.value as? ProfileUiState.Success)?.userDetails
        _uiState.value = ProfileUiState.Success(user = userAuth, userDetails = currentDetails)
    }

    fun getUserDetails(username: String) {
        viewModelScope.launch {
            try {
                repository.getUserDetails(username)
                    .thenAccept { details ->
                        val currentUser = (_uiState.value as? ProfileUiState.Success)?.user
                        _uiState.value = ProfileUiState.Success(
                            user = currentUser,
                            userDetails = details
                        )
                    }
                    .exceptionally { e ->
                        Log.e(tag, "Usuario no tiene detalles guardados o error: ${e.message}")
                        _uiState.value = ProfileUiState.Error(e.localizedMessage ?: "Error al obtener detalles")
                        null
                    }
            } catch (e: Exception) {
                Log.e(tag, "Usuario no tiene detalles guardados o error: ${e.message}")
                _uiState.value = ProfileUiState.Error(e.localizedMessage ?: "Error al obtener detalles")
            }
        }
    }
}
