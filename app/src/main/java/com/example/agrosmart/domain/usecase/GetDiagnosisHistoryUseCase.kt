package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.models.DiagnosisHistory
import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDiagnosisHistoryUseCase @Inject constructor(
    private val repository: DiagnosisHistoryRepository
) {
    operator fun invoke(): Flow<List<DiagnosisHistory>> {
        return repository.getAllHistoriesFlow()
    }

    suspend fun getRecent(limit: Int = 10): Result<List<DiagnosisHistory>> {
        return repository.getRecentHistories(limit)
    }
}
