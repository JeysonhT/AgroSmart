package com.example.agrosmart.core.utils.interfaces

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import com.example.agrosmart.data.local.dto.MMLResultDTO
import com.example.agrosmart.domain.models.DetectionResult
import com.example.agrosmart.domain.models.DiagnosisHistory
import com.example.agrosmart.domain.models.MMLStats
import com.example.agrosmart.domain.models.Respuesta
import org.tensorflow.lite.support.image.TensorImage
import java.util.function.Consumer

interface IDetectionViewModel {
    fun getRecommendationResponse(): LiveData<Respuesta?>
    fun getLastDiagnosis(): LiveData<DiagnosisHistory?>
    fun obtenerRecomendacion(problema: String)
    fun saveDiagnosis(diagnosis: String, image: ByteArray, onSave: Consumer<DiagnosisHistory>?)
    fun getHistory(): LiveData<List<DiagnosisHistory>>
    fun historiesFromUseCase()
    fun addNewHistory(newHistory: DiagnosisHistory)
    fun deleteHistory(_id: String)
    fun saveRecommendationInDiagnosis(_id: String, value: String)
    fun refreshData()
    fun cleanRecommendation()
    fun sendStatsToFirebase(stats: MMLStats)
    fun sendResulToFirebase(result: DetectionResult)
    fun bitmapToTensor(bitmap: Bitmap): TensorImage
    fun processDetection(image: TensorImage, context: Context): MMLResultDTO
}
