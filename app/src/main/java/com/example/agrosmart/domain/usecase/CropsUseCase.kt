package com.example.agrosmart.domain.usecase

import com.example.agrosmart.domain.models.Crop
import com.example.agrosmart.domain.repository.CropRepository
import javax.inject.Inject

class CropsUseCase @Inject constructor(
    private val repository: CropRepository
) {
    suspend operator fun invoke(): List<Crop> {
        return repository.getCrops()
    }

    suspend fun getCrops(): List<Crop> {
        return repository.getCrops()
    }

    suspend fun getCropByName(name: String?): List<Crop> {
        return repository.getCropByName(name)
    }
}
