package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository
import javax.inject.Inject

class UpdateDiagnosisRecommendationUseCase @Inject constructor(
    private val repository: DiagnosisHistoryRepository
) {
    suspend operator fun invoke(id: String, recommendation: String): Result<Unit> {
        require(id.isNotBlank()) { "El id no puede estar vacío" }
        return repository.updateDiagnosisRecommendation(id, recommendation)
    }
}
