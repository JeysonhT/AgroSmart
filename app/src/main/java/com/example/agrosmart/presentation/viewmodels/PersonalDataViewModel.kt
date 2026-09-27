package com.example.agrosmart.presentation.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrosmart.domain.usecase.GetDiagnosisHistoryUseCase
import com.example.agrosmart.presentation.viewmodels.state.PersonalDataUiState
import com.opencsv.CSVWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(
    private val getDiagnosisHistoryUseCase: GetDiagnosisHistoryUseCase
) : ViewModel() {

    private val tag = "PERSONAL_DATA_VIEW_MODEL"

    // Modern StateFlow UI State (UDF) - Single Source of Truth
    private val _uiState = MutableStateFlow<PersonalDataUiState>(PersonalDataUiState.Idle)
    val uiState: StateFlow<PersonalDataUiState> = _uiState.asStateFlow()

    init {
        loadDiagnosisHistory()
    }

    fun loadDiagnosisHistory() {
        _uiState.value = PersonalDataUiState.Loading
        viewModelScope.launch {
            try {
                getDiagnosisHistoryUseCase()
                    .catch { e ->
                        Log.e(tag, "Error al cargar historial de diagnósticos: ${e.message}", e)
                        _uiState.value = PersonalDataUiState.Error(e.localizedMessage ?: "Error al cargar datos")
                    }
                    .collect { nonNullHistories ->
                        var savedCount = 0
                        var diagnosisCount = 0
                        val cropCounts = mutableMapOf<String, Int>()
                        val deficiencyCounts = mutableMapOf<String, Int>()

                        for (history in nonNullHistories) {
                            if (!history.deficiency.isNullOrEmpty()) {
                                diagnosisCount++
                            }

                            if (!history.recommendation.isNullOrBlank()) {
                                savedCount++
                            }

                            val cropName = history.crop?.cropName
                            if (!cropName.isNullOrBlank()) {
                                cropCounts[cropName] = (cropCounts[cropName] ?: 0) + 1
                            }

                            val deficiency = history.deficiency
                            if (!deficiency.isNullOrBlank()) {
                                deficiencyCounts[deficiency] = (deficiencyCounts[deficiency] ?: 0) + 1
                            }
                        }

                        val topCrops = cropCounts.entries
                            .sortedByDescending { it.value }
                            .take(3)
                            .map { it.key }

                        val topDeficiencies = deficiencyCounts.entries
                            .sortedByDescending { it.value }
                            .take(3)
                            .map { it.key }

                        _uiState.value = PersonalDataUiState.Success(
                            topCrops = topCrops,
                            topDeficiencies = topDeficiencies,
                            diagnosisGenerated = diagnosisCount,
                            recommendationsSaved = savedCount,
                            diagnosisHistory = nonNullHistories
                        )
                    }
            } catch (e: Exception) {
                Log.e(tag, "Error al cargar historial: ${e.message}", e)
                _uiState.value = PersonalDataUiState.Error(e.localizedMessage ?: "Error desconocido")
            }
        }
    }

    fun exportDiagnosisHistoryToCsv(context: Context, uri: Uri) {
        val successState = _uiState.value as? PersonalDataUiState.Success
        val histories = successState?.diagnosisHistory
        if (histories.isNullOrEmpty()) {
            Log.w(tag, "No hay historial disponible para exportar.")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    OutputStreamWriter(outputStream).use { writer ->
                        CSVWriter(writer).use { csvWriter ->
                            val header = arrayOf("Fecha", "Cultivo", "Deficiencia", "Recomendacion")
                            csvWriter.writeNext(header)

                            for (history in histories) {
                                val row = arrayOf(
                                    history.diagnosisDate?.toString().orEmpty(),
                                    history.crop?.cropName.orEmpty(),
                                    history.deficiency.orEmpty(),
                                    history.recommendation.orEmpty()
                                )
                                csvWriter.writeNext(row)
                            }
                            Log.i(tag, "Archivo CSV exportado exitosamente a $uri")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(tag, "Error al exportar archivo CSV", e)
            }
        }
    }
}
