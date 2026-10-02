package com.example.agrosmart.domain.usecase

import com.example.agrosmart.core.utils.interfaces.DiagnosisHistoryCallback
import com.example.agrosmart.domain.models.Crop
import com.example.agrosmart.domain.models.DiagnosisHistory
import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.Date
import java.util.concurrent.CompletableFuture

class DiagnosisHistoryUseCaseKotlinTest {

    private lateinit var fakeRepository: FakeDiagnosisHistoryRepository
    private lateinit var getDiagnosisHistoryUseCase: GetDiagnosisHistoryUseCase
    private lateinit var saveDiagnosisUseCase: SaveDiagnosisUseCase
    private lateinit var deleteDiagnosisUseCase: DeleteDiagnosisUseCase
    private lateinit var updateDiagnosisRecommendationUseCase: UpdateDiagnosisRecommendationUseCase

    @Before
    fun setUp() {
        fakeRepository = FakeDiagnosisHistoryRepository()
        getDiagnosisHistoryUseCase = GetDiagnosisHistoryUseCase(fakeRepository)
        saveDiagnosisUseCase = SaveDiagnosisUseCase(fakeRepository)
        deleteDiagnosisUseCase = DeleteDiagnosisUseCase(fakeRepository)
        updateDiagnosisRecommendationUseCase = UpdateDiagnosisRecommendationUseCase(fakeRepository)
    }

    @Test
    fun `save and get diagnosis histories flow emits saved items`() = runTest {
        val crop = Crop(cropName = "Maíz", description = "Cultivo de grano", harvestTime = "90 días", type = "Cereal")
        val diagnosis = DiagnosisHistory(
            _id = "test-id-1",
            crop = crop,
            diagnosisDate = Date(),
            deficiency = "Deficiencia de Nitrógeno",
            recommendation = "Aplicar Urea"
        )

        val saveResult = saveDiagnosisUseCase(diagnosis)
        assertTrue(saveResult.isSuccess)

        val histories = getDiagnosisHistoryUseCase().first()
        assertEquals(1, histories.size)
        assertEquals("test-id-1", histories[0].id)
        assertEquals("Deficiencia de Nitrógeno", histories[0].deficiency)
        assertEquals("Maíz", histories[0].crop?.cropName)
    }

    @Test
    fun `update diagnosis recommendation modifies recommendation in repository`() = runTest {
        val diagnosis = DiagnosisHistory(_id = "test-id-2", deficiency = "Potasio", recommendation = "Inicial")
        saveDiagnosisUseCase(diagnosis)

        val updateResult = updateDiagnosisRecommendationUseCase("test-id-2", "Recomendación actualizada")
        assertTrue(updateResult.isSuccess)

        val histories = getDiagnosisHistoryUseCase().first()
        assertEquals("Recomendación actualizada", histories.find { it.id == "test-id-2" }?.recommendation)
    }

    @Test
    fun `delete diagnosis removes item from repository`() = runTest {
        val diagnosis = DiagnosisHistory(_id = "test-id-3", deficiency = "Fósforo")
        saveDiagnosisUseCase(diagnosis)

        val deleteResult = deleteDiagnosisUseCase("test-id-3")
        assertTrue(deleteResult.isSuccess)

        val histories = getDiagnosisHistoryUseCase().first()
        assertTrue(histories.none { it.id == "test-id-3" })
    }

    // Fake in-memory repository for fast, deterministic, pure unit testing
    class FakeDiagnosisHistoryRepository : DiagnosisHistoryRepository {
        private val list = mutableListOf<DiagnosisHistory>()

        override fun getAllHistoriesFlow(): Flow<List<DiagnosisHistory>> {
            return flowOf(list.toList())
        }

        override suspend fun getRecentHistories(limit: Int): Result<List<DiagnosisHistory>> {
            return Result.success(list.take(limit))
        }

        override suspend fun getLastDiagnosisSuspend(): Result<DiagnosisHistory?> {
            return Result.success(list.firstOrNull())
        }

        override suspend fun insertDiagnosis(history: DiagnosisHistory): Result<Unit> {
            list.add(0, history)
            return Result.success(Unit)
        }

        override suspend fun updateDiagnosisRecommendation(id: String, recommendation: String): Result<Unit> {
            val idx = list.indexOfFirst { it.id == id }
            if (idx >= 0) {
                list[idx] = list[idx].copy(recommendation = recommendation)
            }
            return Result.success(Unit)
        }

        override suspend fun deleteDiagnosisById(id: String): Result<Unit> {
            list.removeIf { it.id == id }
            return Result.success(Unit)
        }

        override suspend fun getDiagnosisHistories(): List<DiagnosisHistory> {
            return list.toList()
        }

        override suspend fun getLastDiagnosis(): DiagnosisHistory {
            return list.firstOrNull() ?: DiagnosisHistory()
        }

        override suspend fun saveDiagnosis(history: DiagnosisHistory, callback: DiagnosisHistoryCallback): List<DiagnosisHistory> {
            list.add(0, history)
            callback.onLoaded(listOf(history))
            return list.toList()
        }

        override suspend fun updateDiagnosis(_id: String, param: String, value: String) {
            val idx = list.indexOfFirst { it.id == _id }
            if (idx >= 0) {
                list[idx] = list[idx].copy(recommendation = value)
            }
        }

        override suspend fun deleteDiagnosis(_id: String) {
            list.removeIf { it.id == _id }
        }
    }
}
