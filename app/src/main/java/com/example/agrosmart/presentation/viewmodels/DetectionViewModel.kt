package com.example.agrosmart.presentation.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrosmart.core.utils.interfaces.CropsCallback
import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback
import com.example.agrosmart.core.utils.interfaces.IDetectionViewModel
import com.example.agrosmart.data.local.dto.MMLResultDTO
import com.example.agrosmart.domain.models.Crop
import com.example.agrosmart.domain.models.DetectionResult
import com.example.agrosmart.domain.models.DiagnosisHistory
import com.example.agrosmart.domain.models.MMLStats
import com.example.agrosmart.domain.models.Respuesta
import com.example.agrosmart.domain.usecase.CropsUseCase
import com.example.agrosmart.domain.usecase.DeleteDiagnosisUseCase
import com.example.agrosmart.domain.usecase.DetectionResultUseCase
import com.example.agrosmart.domain.usecase.DetectionUseCase
import com.example.agrosmart.domain.usecase.DiagnosisHistoryUseCase
import com.example.agrosmart.domain.usecase.GetDiagnosisHistoryUseCase
import com.example.agrosmart.domain.usecase.GetRecommendationUseCase
import com.example.agrosmart.domain.usecase.MMLStatsUseCase
import com.example.agrosmart.domain.usecase.SaveDiagnosisUseCase
import com.example.agrosmart.domain.usecase.UpdateDiagnosisRecommendationUseCase
import com.example.agrosmart.domain.usecase.UserDtlUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import java.util.Date
import java.util.UUID
import java.util.function.Consumer
import javax.inject.Inject

@HiltViewModel
open class DetectionViewModel @Inject constructor(
    protected val getDiagnosisHistoryUseCase: GetDiagnosisHistoryUseCase,
    protected val saveDiagnosisUseCase: SaveDiagnosisUseCase,
    protected val deleteDiagnosisUseCase: DeleteDiagnosisUseCase,
    protected val updateDiagnosisRecommendationUseCase: UpdateDiagnosisRecommendationUseCase,
    protected val getRecommendationUseCase: GetRecommendationUseCase,
    protected var cropsUseCase: CropsUseCase,
    protected val detectionUseCase: DetectionUseCase,
    protected val mmlUseCase: MMLStatsUseCase,
    protected val drUseCase: DetectionResultUseCase,
    protected val userDtlUseCase: UserDtlUseCase? = null,
    protected val firebaseAuth: FirebaseAuth? = null
) : ViewModel(), IDetectionViewModel {

    protected val TAG = "DETECTION_VIEW_MODEL"

    // Modern StateFlow UI State (UDF)
    private val _uiState = MutableStateFlow<DetectionUiState>(DetectionUiState.Idle)
    val uiState: StateFlow<DetectionUiState> = _uiState.asStateFlow()

    // Backward-compatible LiveData for existing Java fragments & tests
    private val _histories = MutableLiveData<List<DiagnosisHistory>>()
    private val _lastDiagnosis = MutableLiveData<DiagnosisHistory?>()
    private val _recommendationResponse = MutableLiveData<Respuesta?>()

    // Legacy bridges for testing
    var legacyDiagnosisHistoryUseCase: DiagnosisHistoryUseCase? = null
    var legacyRecommendationUseCase: GetRecommendationUseCase? = null

    init {
        observeHistories()
    }

    protected fun observeHistories() {
        viewModelScope.launch {
            if (legacyDiagnosisHistoryUseCase != null) return@launch
            try {
                getDiagnosisHistoryUseCase()
                    .catch { e ->
                        Log.e(TAG, "Error collecting history: ${e.message}", e)
                        _uiState.value = DetectionUiState.Error(e.localizedMessage ?: "Error al cargar historial")
                    }
                    .collect { list ->
                        if (legacyDiagnosisHistoryUseCase != null) return@collect
                        _histories.value = list
                        val latest = list.firstOrNull()
                        _lastDiagnosis.value = latest
                        _uiState.value = DetectionUiState.Success(
                            diagnosis = latest,
                            recommendation = _recommendationResponse.value?.respuesta.orEmpty(),
                            histories = list
                        )
                    }
            } catch (e: Exception) {
                Log.w(TAG, "History flow could not be observed: ${e.message}")
            }
        }
    }

    override fun getRecommendationResponse(): LiveData<Respuesta?> = _recommendationResponse

    override fun getLastDiagnosis(): LiveData<DiagnosisHistory?> = _lastDiagnosis

    override fun getHistory(): LiveData<List<DiagnosisHistory>> = _histories

    override fun obtenerRecomendacion(problema: String) {
        _uiState.value = DetectionUiState.Loading

        val recUseCase = legacyRecommendationUseCase ?: getRecommendationUseCase

        var soil = ""
        try {
            val email = firebaseAuth?.currentUser?.email
            if (!email.isNullOrBlank() && userDtlUseCase != null) {
                soil = userDtlUseCase.getSoilTypeFromDetail(email) ?: ""
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting soil: ${e.message}")
        }

        val pregunta = "Comportate como un agronomo profesional y " +
                "genera recomendaciones para el siguiente problema\n" +
                problema + "\n" + "plantado en este tipo de suelo: " + soil + " de estar vacio el tipo de suelo omite el detalle\n" +
                "recomienda fertilizantes organicos y no organicos, " +
                "no excedas las 150 palabras, las listas crealas usando guiones y evita el uso de ateriscos para titulos y para los nombres de la soluciones, " +
                "dejalos separados de los parrafos para obtener un texto mas limpio"

        try {
            recUseCase.ejecutar(pregunta).thenAccept { respuesta ->
                _recommendationResponse.postValue(respuesta)
                val currentHistories = _histories.value.orEmpty()
                _uiState.value = DetectionUiState.Success(
                    diagnosis = _lastDiagnosis.value,
                    recommendation = respuesta?.respuesta.orEmpty(),
                    histories = currentHistories
                )
            }.exceptionally { error ->
                Log.e(TAG, "Error al obtener recomendación", error)
                val errorResp = Respuesta("error")
                _recommendationResponse.postValue(errorResp)
                _uiState.value = DetectionUiState.Error(error.localizedMessage ?: "Error al obtener recomendación")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception executing recommendation: ${e.message}", e)
            _recommendationResponse.postValue(Respuesta("error"))
            _uiState.value = DetectionUiState.Error(e.localizedMessage ?: "Error al obtener recomendación")
        }
    }

    override fun saveDiagnosis(diagnosis: String, image: ByteArray, onSave: Consumer<DiagnosisHistory>?) {
        val name = diagnosis.split(" ").firstOrNull() ?: ""
        cropsUseCase.getCropByName(name, object : CropsCallback {
            override fun onCropsLoaded(crops: List<Crop>?) {
                if (crops.isNullOrEmpty()) {
                    onError(Exception("Crop not found"))
                    return
                }
                val crop = crops[0]
                val diagnosisHistory = DiagnosisHistory.builder()
                    ._id(UUID.randomUUID().toString())
                    .diagnosisDate(Date())
                    .Crop(crop)
                    .deficiency(diagnosis)
                    .image(image)
                    .recommendation("")
                    .lastUpdate(System.currentTimeMillis())
                    .build()

                onSave?.accept(diagnosisHistory)

                if (legacyDiagnosisHistoryUseCase != null) {
                    legacyDiagnosisHistoryUseCase?.saveDiagnosis(diagnosisHistory, object : DiagnosisHistoryCallback {
                        override fun onLoaded(history: List<DiagnosisHistory>?) {
                            if (!history.isNullOrEmpty()) {
                                _lastDiagnosis.postValue(history[0])
                            }
                            Log.d(TAG, "Diagnosis saved successfully")
                        }

                        override fun onError(e: Exception?) {
                            Log.e(TAG, "Error saving diagnosis", e)
                        }
                    })
                } else {
                    viewModelScope.launch {
                        saveDiagnosisUseCase(diagnosisHistory).onSuccess {
                            _lastDiagnosis.postValue(diagnosisHistory)
                            Log.d(TAG, "Diagnosis saved successfully")
                        }.onFailure { e ->
                            Log.e(TAG, "Error saving diagnosis", e)
                        }
                    }
                }
            }

            override fun onError(e: Exception) {
                Log.e(TAG, "Error getting crop by name", e)
                _uiState.value = DetectionUiState.Error(e.localizedMessage ?: "Error al buscar cultivo")
            }
        })
    }

    override fun gethistoriesFromUseCase() {
        if (legacyDiagnosisHistoryUseCase != null) {
            legacyDiagnosisHistoryUseCase?.histories?.thenAccept { values ->
                _histories.postValue(values ?: emptyList())
                if (!values.isNullOrEmpty()) {
                    _lastDiagnosis.postValue(values[0])
                } else {
                    _lastDiagnosis.postValue(null)
                }
            }?.exceptionally {
                _histories.postValue(emptyList())
                _lastDiagnosis.postValue(null)
                null
            }
        } else {
            observeHistories()
        }
    }

    override fun addNewHistory(newHistory: DiagnosisHistory) {
        val currentList = _histories.value.orEmpty().toMutableList()
        currentList.add(0, newHistory)
        _histories.value = currentList
        _lastDiagnosis.value = newHistory
    }

    override fun deleteHistory(id: String) {
        if (legacyDiagnosisHistoryUseCase != null) {
            legacyDiagnosisHistoryUseCase?.deleteDiagnosis(id)
        } else {
            viewModelScope.launch {
                deleteDiagnosisUseCase(id)
            }
        }
    }

    override fun saveRecommendationInDiagnosis(id: String, value: String) {
        if (legacyDiagnosisHistoryUseCase != null) {
            legacyDiagnosisHistoryUseCase?.updateDiagnosis(id, value)
        } else {
            viewModelScope.launch {
                updateDiagnosisRecommendationUseCase(id, value)
            }
        }
    }

    override fun refreshData() {
        gethistoriesFromUseCase()
    }

    override fun cleanRecommendation() {
        _recommendationResponse.postValue(null)
    }

    override fun sendStatsToFirebase(stats: MMLStats) {
        mmlUseCase.saveStats(stats)
    }

    override fun sendResulToFirebase(result: DetectionResult) {
        drUseCase.saveResult(result)
    }

    override fun bitmapToTensor(bitmap: Bitmap): TensorImage {
        return detectionUseCase.bitMapToTensor(bitmap)
    }

    override fun processDetection(image: TensorImage, context: Context): MMLResultDTO {
        return detectionUseCase.processDetection(image, context)
    }
}
