package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.models.Deficiency
import com.example.agrosmart.domain.repository.DeficiencyRepository
import javax.inject.Inject

class DeficiencyUseCase @Inject constructor(
    private val repository: DeficiencyRepository
) {
    suspend fun getDeficienciesAsync(): List<Deficiency> {
        return repository.getDeficiencies()
    }
}
