package com.example.agrosmart.presentation.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.agrosmart.domain.models.Deficiency
import com.example.agrosmart.domain.usecase.DeficiencyUseCase
import com.example.agrosmart.presentation.viewmodels.state.DeficiencyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DeficiencyViewModel @Inject constructor(
    private val deficiencyUseCase: DeficiencyUseCase
) : ViewModel() {

    // Constructor secundario para compatibilidad sin Hilt
    constructor() : this(DeficiencyUseCase())

    private val tag = "DEFICIENCY_VIEW_MODEL"

    // Modern StateFlow UI State (UDF) - Single Source of Truth
    private val _uiState = MutableStateFlow<DeficiencyUiState>(DeficiencyUiState.Idle)
    val uiState: StateFlow<DeficiencyUiState> = _uiState.asStateFlow()

    fun loadData(context: Context? = null) {
        _uiState.value = DeficiencyUiState.Loading
        try {
            deficiencyUseCase.getDeficiencies(context)
                .thenAccept { list ->
                    val result = list ?: emptyList()
                    _uiState.value = DeficiencyUiState.Success(result)
                }
                .exceptionally { e ->
                    Log.e(tag, "Error al obtener los datos: ${e.message}")
                    _uiState.value = DeficiencyUiState.Error(e.localizedMessage ?: "Error al obtener los datos")
                    null
                }
        } catch (e: Exception) {
            Log.e(tag, "Error al obtener los datos: ${e.message}", e)
            _uiState.value = DeficiencyUiState.Error(e.localizedMessage ?: "Error al obtener los datos")
        }
    }
}
