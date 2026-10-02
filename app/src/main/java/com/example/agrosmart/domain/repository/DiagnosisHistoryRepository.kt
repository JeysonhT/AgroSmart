package com.example.agrosmart.domain.repository

import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback
import com.example.agrosmart.domain.models.DiagnosisHistory
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.CompletableFuture

interface DiagnosisHistoryRepository {
    // Contrato moderno Clean Architecture (Kotlin Coroutines & Flow)
    fun getAllHistoriesFlow(): Flow<List<DiagnosisHistory>>
    suspend fun getRecentHistories(limit: Int = 10): Result<List<DiagnosisHistory>>
    suspend fun getLastDiagnosisSuspend(): Result<DiagnosisHistory?>
    suspend fun insertDiagnosis(history: DiagnosisHistory): Result<Unit>
    suspend fun updateDiagnosisRecommendation(id: String, recommendation: String): Result<Unit>
    suspend fun deleteDiagnosisById(id: String): Result<Unit>

    // Métodos para compatibilidad regresiva con componentes Java legacy
    suspend fun getDiagnosisHistories(): List<DiagnosisHistory>
    suspend fun getLastDiagnosis(): DiagnosisHistory
    suspend fun saveDiagnosis(history: DiagnosisHistory, callback: DiagnosisHistoryCallback): List<DiagnosisHistory>
    suspend fun updateDiagnosis(_id: String, param: String, value: String)
    suspend fun deleteDiagnosis(_id: String)
}
