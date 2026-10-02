package com.example.agrosmart.presentation.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agrosmart.core.utils.interfaces.IDetectionViewModel
import com.example.agrosmart.data.local.dto.MMLResultDTO
import com.example.agrosmart.domain.models.DetectionResult
import com.example.agrosmart.domain.models.DiagnosisHistory
import com.example.agrosmart.domain.models.MMLStats
import com.example.agrosmart.domain.models.Respuesta
import com.example.agrosmart.domain.usecase.CropsUseCase
import com.example.agrosmart.domain.usecase.DeleteDiagnosisUseCase
import com.example.agrosmart.domain.usecase.DetectionResultUseCase
import com.example.agrosmart.domain.usecase.DetectionUseCase
import com.example.agrosmart.domain.usecase.GetDiagnosisHistoryUseCase
import com.example.agrosmart.domain.usecase.GetRecommendationUseCase
import com.example.agrosmart.domain.usecase.MMLStatsUseCase
import com.example.agrosmart.domain.usecase.SaveDiagnosisUseCase
import com.example.agrosmart.domain.usecase.UpdateDiagnosisRecommendationUseCase
import com.example.agrosmart.domain.usecase.UserDtlUseCase
import com.example.agrosmart.presentation.viewmodels.state.DetectionUiState
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
class DetectionViewModel @Inject constructor(
    private val getDiagnosisHistoryUseCase: GetDiagnosisHistoryUseCase,
    private val saveDiagnosisUseCase: SaveDiagnosisUseCase,
    private val deleteDiagnosisUseCase: DeleteDiagnosisUseCase,
    private val updateDiagnosisRecommendationUseCase: UpdateDiagnosisRecommendationUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val cropsUseCase: CropsUseCase,
    private val detectionUseCase: DetectionUseCase,
    private val mmlUseCase: MMLStatsUseCase,
    private val drUseCase: DetectionResultUseCase,
    private val userDtlUseCase: UserDtlUseCase? = null,
    private val firebaseAuth: FirebaseAuth? = null
) : ViewModel(), IDetectionViewModel {

    private val TAG = "DETECTION_VIEW_MODEL"

    // Modern StateFlow UI State (UDF)
    private val _uiState = MutableStateFlow<DetectionUiState>(DetectionUiState.Idle)
    val uiState: StateFlow<DetectionUiState> = _uiState.asStateFlow()

    // Backward-compatible LiveData for existing observers & tests
    private val _histories = MutableLiveData<List<DiagnosisHistory>>()
    private val _lastDiagnosis = MutableLiveData<DiagnosisHistory?>()
    private val _recommendationResponse = MutableLiveData<Respuesta?>()

    init {
        observeHistories()
    }

    fun observeHistories() {
        viewModelScope.launch {
            try {
                getDiagnosisHistoryUseCase()
                    .catch { e ->
                        Log.e(TAG, "Error collecting history: ${e.message}", e)
                        _uiState.value = DetectionUiState.Error(e.localizedMessage ?: "Error al cargar historial")
                    }
                    .collect { list ->
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

        viewModelScope.launch {

            _uiState.value = DetectionUiState.Loading

            try {
                val recommendation = getRecommendationUseCase.ejecutar(pregunta)

                _recommendationResponse.postValue(recommendation)

                val currentHistories = _histories.value.orEmpty()

                _uiState.value = DetectionUiState.Success(
                        diagnosis = _lastDiagnosis.value,
                        recommendation = recommendation?.respuesta.orEmpty(),
                        histories = currentHistories
                )
            } catch (e: Exception){
                Log.e(TAG, "Exception executing recommendation: ${e.message}", e)
                _recommendationResponse.postValue(Respuesta("error"))
                _uiState.value = DetectionUiState.Error(e.localizedMessage ?: "Error al obtener recomendación")
            }

        }
    }

    override fun saveDiagnosis(diagnosis: String, image: ByteArray, onSave: Consumer<DiagnosisHistory>?) {
        val name = diagnosis.split(" ").firstOrNull() ?: ""

        viewModelScope.launch {
            val cropByName = cropsUseCase.getCropByName(
                    name,
            )

            if(cropByName.isEmpty()) {
                _uiState.value = DetectionUiState.Error("El cultivo no existe")
            }

            val crop = cropByName[0]
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

            saveDiagnosisUseCase(diagnosisHistory).onSuccess {
                _lastDiagnosis.postValue(diagnosisHistory)
                Log.d(TAG, "Diagnosis saved successfully")
            }.onFailure { e ->
                Log.e(TAG, "Error saving diagnosis", e)
            }

        }

    }

    override fun historiesFromUseCase() {
        observeHistories()
    }

    override fun addNewHistory(newHistory: DiagnosisHistory) {
        val currentList = _histories.value.orEmpty().toMutableList()
        currentList.add(0, newHistory)
        _histories.value = currentList
        _lastDiagnosis.value = newHistory
    }

    override fun deleteHistory(id: String) {
        viewModelScope.launch {
            deleteDiagnosisUseCase(id)
        }
    }

    override fun saveRecommendationInDiagnosis(id: String, value: String) {
        viewModelScope.launch {
            updateDiagnosisRecommendationUseCase(id, value)
        }
    }

    override fun refreshData() {
        historiesFromUseCase()
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
        return detectionUseCase.processDetection(image)
    }
}
