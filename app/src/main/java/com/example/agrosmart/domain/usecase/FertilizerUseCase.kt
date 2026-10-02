package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.models.Fertilizer
import com.example.agrosmart.domain.repository.FertilizerRepository
import javax.inject.Inject

class FertilizerUseCase @Inject constructor(
    private val repository: FertilizerRepository
) {
    suspend operator fun invoke(): List<Fertilizer> {
        return repository.getFertilizers()
    }
}
