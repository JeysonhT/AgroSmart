package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.repository.DiagnosisHistoryRepository
import javax.inject.Inject

class DeleteDiagnosisUseCase @Inject constructor(
    private val repository: DiagnosisHistoryRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        require(id.isNotBlank()) { "El id no puede estar vacío" }
        return repository.deleteDiagnosisById(id)
    }
}
