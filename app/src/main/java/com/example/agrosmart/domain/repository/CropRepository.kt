package com.example.agrosmart.domain.repository

import com.example.agrosmart.domain.models.Crop

interface CropRepository {
    suspend fun getCrops(): List<Crop>
    suspend fun getCropByName(name: String?): List<Crop>
}
