package com.example.agrosmart.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrosmart.domain.usecase.FertilizerUseCase
import com.example.agrosmart.presentation.viewmodels.state.FertilizerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FertilizerViewModel @Inject constructor(
    private val fertilizerUseCase: FertilizerUseCase
) : ViewModel() {

    private val tag = "FERTILIZER_VIEW_MODEL"

    // Modern StateFlow UI State (UDF) - Single Source of Truth
    private val _uiState = MutableStateFlow<FertilizerUiState>(FertilizerUiState.Idle)
    val uiState: StateFlow<FertilizerUiState> = _uiState.asStateFlow()

    fun loadData() {
        _uiState.value = FertilizerUiState.Loading

        viewModelScope.launch {
            try {
                val data = fertilizerUseCase()
                _uiState.value = FertilizerUiState.Success(data)
            } catch (e: Exception) {
                Log.e(tag, "Error al obtener los datos: ${e.message}", e)
                _uiState.value = FertilizerUiState.Error(e.localizedMessage ?: "Error al obtener los datos")
            }
        }
    }
}
