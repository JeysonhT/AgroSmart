package com.example.agrosmart.data.repository.impl

import android.util.Log
import com.example.agrosmart.AgroSmartApp
import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback
import com.example.agrosmart.data.local.room.dao.DiagnosisHistoryDao
import com.example.agrosmart.data.mapper.toDomain
import com.example.agrosmart.data.mapper.toEntity
import com.example.agrosmart.domain.models.DiagnosisHistory
import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.CompletableFuture
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiagnosisHistoryLocalRepositoryImpl @Inject constructor(
    private val dao: DiagnosisHistoryDao
) : DiagnosisHistoryRepository {

    private val TAG = "DIAGNOSIS_HISTORY_REPOSITORY"
    private val scope = CoroutineScope(Dispatchers.IO)

    // Constructor sin argumentos para compatibilidad con código Java legado
    constructor() : this(
        try {
            AgroSmartApp.database.diagnosisHistoryDao()
        } catch (e: Exception) {
            // En pruebas unitarias donde AgroSmartApp.instance no esté inicializada
            object : DiagnosisHistoryDao {
                private val memList = mutableListOf<com.example.agrosmart.data.local.room.entity.DiagnosisHistoryEntity>()

                override fun getAllHistoriesFlow() = kotlinx.coroutines.flow.flowOf(memList.toList())
                override suspend fun getAllHistories() = memList.toList()
                override suspend fun getRecentHistories(limit: Int) = memList.take(limit)
                override fun getLastDiagnosisFlow() = kotlinx.coroutines.flow.flowOf(memList.firstOrNull())
                override suspend fun getLastDiagnosis() = memList.firstOrNull()
                override suspend fun getDiagnosisById(id: String) = memList.find { it.id == id }
                override suspend fun insert(diagnosis: com.example.agrosmart.data.local.room.entity.DiagnosisHistoryEntity): Long {
                    memList.add(0, diagnosis)
                    return 1L
                }
                override suspend fun insertAll(diagnoses: List<com.example.agrosmart.data.local.room.entity.DiagnosisHistoryEntity>) {
                    memList.addAll(0, diagnoses)
                }
                override suspend fun update(diagnosis: com.example.agrosmart.data.local.room.entity.DiagnosisHistoryEntity): Int {
                    val idx = memList.indexOfFirst { it.id == diagnosis.id }
                    if (idx >= 0) {
                        memList[idx] = diagnosis
                        return 1
                    }
                    return 0
                }
                override suspend fun updateRecommendation(id: String, recommendation: String): Int {
                    val item = memList.find { it.id == id }
                    if (item != null) {
                        val updated = item.copy(recommendation = recommendation)
                        update(updated)
                        return 1
                    }
                    return 0
                }
                override suspend fun delete(diagnosis: com.example.agrosmart.data.local.room.entity.DiagnosisHistoryEntity): Int {
                    return if (memList.removeIf { it.id == diagnosis.id }) 1 else 0
                }
                override suspend fun deleteById(id: String): Int {
                    return if (memList.removeIf { it.id == id }) 1 else 0
                }
                override suspend fun deleteAll(): Int {
                    val count = memList.size
                    memList.clear()
                    return count
                }
            }
        }
    )

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

    override fun getDiagnosisHistories(): CompletableFuture<List<DiagnosisHistory>> {
        val future = CompletableFuture<List<DiagnosisHistory>>()
        scope.launch {
            try {
                val entities = dao.getRecentHistories(10)
                val domainList = entities.map { it.toDomain() }
                future.complete(domainList)
            } catch (e: Exception) {
                Log.w(TAG, "Error al obtener historiales: ${e.message}")
                future.completeExceptionally(e)
            }
        }
        return future
    }

    override fun getLastDiagnosis(): DiagnosisHistory {
        return runBlocking(Dispatchers.IO) {
            try {
                dao.getLastDiagnosis()?.toDomain() ?: DiagnosisHistory()
            } catch (e: Exception) {
                Log.w(TAG, "Error al obtener el último diagnóstico: ${e.message}")
                DiagnosisHistory()
            }
        }
    }

    override fun saveDiagnosis(history: DiagnosisHistory, callback: DiagnosisHistoryCallback) {
        scope.launch {
            try {
                dao.insert(history.toEntity())
                val lastList = mutableListOf(history)
                callback.onLoaded(lastList)
            } catch (e: Exception) {
                Log.w(TAG, "Error al guardar el diagnóstico: ${e.message}")
                callback.onError(e)
            }
        }
    }

    override fun updateDiagnosis(_id: String, param: String, value: String) {
        scope.launch {
            try {
                dao.updateRecommendation(_id, value)
            } catch (e: Exception) {
                Log.w(TAG, "Error al actualizar recomendación: ${e.message}")
            }
        }
    }

    override fun deleteDiagnosis(_id: String) {
        scope.launch {
            try {
                dao.deleteById(_id)
            } catch (e: Exception) {
                Log.w(TAG, "Error al eliminar diagnóstico: ${e.message}")
            }
        }
    }
}
