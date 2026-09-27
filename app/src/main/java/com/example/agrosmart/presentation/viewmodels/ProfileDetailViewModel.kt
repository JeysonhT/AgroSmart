package com.example.agrosmart.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrosmart.core.utils.classes.NetworkChecker
import com.example.agrosmart.data.repository.impl.UserDtlimpl
import com.example.agrosmart.domain.models.UserDetails
import com.example.agrosmart.domain.repository.UserDtlRepository
import com.example.agrosmart.domain.usecase.UserDtlUseCase
import com.example.agrosmart.presentation.viewmodels.state.ProfileDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val useCase: UserDtlUseCase,
    private val udRepository: UserDtlRepository
) : ViewModel() {

    // Constructor secundario para compatibilidad sin inyección Hilt
    constructor() : this(UserDtlUseCase(), UserDtlimpl())

    private val tag = "PROFILE_DETAIL_VIEWMODEL"

    // Modern StateFlow UI State (UDF) - Single Source of Truth
    private val _uiState = MutableStateFlow<ProfileDetailUiState>(ProfileDetailUiState.Idle)
    val uiState: StateFlow<ProfileDetailUiState> = _uiState.asStateFlow()

    fun postDetails(details: UserDetails, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                udRepository.postUserDetails(details, email)
            } catch (e: Exception) {
                Log.e(tag, "Error al guardar detalles de usuario: ${e.message}", e)
            }
        }
    }

    fun loadUserDetails(username: String, context: Context? = null) {
        _uiState.value = ProfileDetailUiState.Loading
        viewModelScope.launch {
            try {
                val isOnline = context?.let { NetworkChecker.isInternetAvailable(it) } ?: true
                val future = if (isOnline) {
                    useCase.getUserDetails(username)
                } else {
                    useCase.getLocalDetails(username)
                }

                future.thenAccept { details ->
                    _uiState.value = ProfileDetailUiState.Success(details)
                }.exceptionally { ex ->
                    Log.e(tag, "Error al obtener detalles: ${ex.message}")
                    _uiState.value = ProfileDetailUiState.Error(ex.localizedMessage ?: "Error al obtener detalles")
                    null
                }
            } catch (e: Exception) {
                Log.e(tag, "Error inesperado al cargar detalles: ${e.message}", e)
                _uiState.value = ProfileDetailUiState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }
}
