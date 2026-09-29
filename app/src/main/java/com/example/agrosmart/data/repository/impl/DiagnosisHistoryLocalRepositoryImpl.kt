package com.example.agrosmart.data.repository.impl

import android.util.Log
import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback
import com.example.agrosmart.data.local.room.dao.DiagnosisHistoryDao
import com.example.agrosmart.data.mapper.toDomain
import com.example.agrosmart.data.mapper.toEntity
import com.example.agrosmart.di.IoDispatcher
import com.example.agrosmart.domain.models.DiagnosisHistory
import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiagnosisHistoryLocalRepositoryImpl @Inject constructor(
    private val dao: DiagnosisHistoryDao,
        @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DiagnosisHistoryRepository {

    private val TAG = "DIAGNOSIS_HISTORY_REPOSITORY"

    // --- Métodos modernos con Flow y Coroutines ---

    override fun getAllHistoriesFlow(): Flow<List<DiagnosisHistory>> {
        return dao.getAllHistoriesFlow().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getRecentHistories(limit: Int): Result<List<DiagnosisHistory>> = withContext(Dispatchers.IO) {
        runCatching {
            dao.getRecentHistories(limit).map { it.toDomain() }
        }
    }

    override suspend fun getLastDiagnosisSuspend(): Result<DiagnosisHistory?> = withContext(Dispatchers.IO) {
        runCatching {
            dao.getLastDiagnosis()?.toDomain()
        }
    }

    override suspend fun insertDiagnosis(history: DiagnosisHistory): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            dao.insert(history.toEntity())
            Unit
        }
    }

    override suspend fun updateDiagnosisRecommendation(id: String, recommendation: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            dao.updateRecommendation(id, recommendation)
            Unit
        }
    }

    override suspend fun deleteDiagnosisById(id: String): Result<Unit> = withContext(Dispatchers.IO) {
        runCatching {
            dao.deleteById(id)
            Unit
        }
    }

    // --- Métodos legados para compatibilidad regresiva ---

    override suspend fun getDiagnosisHistories(): List<DiagnosisHistory> =
        withContext(ioDispatcher) {
            try {
                val entities = dao.getRecentHistories(10)
                val domainList = entities.map { it.toDomain() }

                domainList
            } catch (e: Exception) {
                Log.w(TAG, "Error al obtener historiales: ${e.message}")
                emptyList()
            }
        }

    override suspend fun getLastDiagnosis(): DiagnosisHistory =
        withContext(ioDispatcher) {
            try {
                dao.getLastDiagnosis()?.toDomain() ?: DiagnosisHistory()
            } catch (e: Exception) {
                Log.w(TAG, "Error al obtener el último diagnóstico: ${e.message}")
                DiagnosisHistory()
            }
        }


    override suspend fun saveDiagnosis(history: DiagnosisHistory, callback: DiagnosisHistoryCallback) =
        withContext(ioDispatcher) {
            try {
                dao.insert(history.toEntity())
                val lastList = mutableListOf(history)
                lastList
            } catch (e: Exception) {
                Log.w(TAG, "Error al guardar el diagnóstico: ${e.message}")
                emptyList()
            }
        }


    override suspend fun updateDiagnosis(_id: String, param: String, value: String) {
        withContext(ioDispatcher) {
            try {
                dao.updateRecommendation(_id, value)
            } catch (e: Exception) {
                Log.w(TAG, "Error al actualizar recomendación: ${e.message}")
            }
        }
    }

    override suspend fun deleteDiagnosis(_id: String) {
        withContext(ioDispatcher) {
            try {
                dao.deleteById(_id)
            } catch (e: Exception) {
                Log.w(TAG, "Error al eliminar diagnóstico: ${e.message}")
            }
        }
    }
}
