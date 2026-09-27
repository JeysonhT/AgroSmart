package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.models.DiagnosisHistory
import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository
import javax.inject.Inject

class SaveDiagnosisUseCase @Inject constructor(
    private val repository: DiagnosisHistoryRepository
) {
    suspend operator fun invoke(history: DiagnosisHistory): Result<Unit> {
        return repository.insertDiagnosis(history)
    }
}
