package com.example.agrosmart.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.agrosmart.domain.models.Fertilizer
import com.example.agrosmart.domain.usecase.FertilizerUseCase
import com.example.agrosmart.presentation.viewmodels.state.FertilizerUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FertilizerViewModel @Inject constructor(
    private val fertilizerUseCase: FertilizerUseCase
) : ViewModel() {

    // Constructor secundario para compatibilidad sin Hilt
    constructor() : this(FertilizerUseCase())

    private val tag = "FERTILIZER_VIEW_MODEL"

    // Modern StateFlow UI State (UDF) - Single Source of Truth
    private val _uiState = MutableStateFlow<FertilizerUiState>(FertilizerUiState.Idle)
    val uiState: StateFlow<FertilizerUiState> = _uiState.asStateFlow()

    fun loadData(context: Context? = null) {
        _uiState.value = FertilizerUiState.Loading
        try {
            fertilizerUseCase.getFertilizers(context)
                .thenAccept { list ->
                    val result = list ?: emptyList()
                    _uiState.value = FertilizerUiState.Success(result)
                }
                .exceptionally { e ->
                    Log.e(tag, "Error al obtener los datos: ${e.message}")
                    _uiState.value = FertilizerUiState.Error(e.localizedMessage ?: "Error al obtener los datos")
                    null
                }
        } catch (e: Exception) {
            Log.e(tag, "Error al obtener los datos: ${e.message}", e)
            _uiState.value = FertilizerUiState.Error(e.localizedMessage ?: "Error al obtener los datos")
        }
    }
}
