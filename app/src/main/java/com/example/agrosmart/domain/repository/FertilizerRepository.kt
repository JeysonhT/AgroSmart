package com.example.agrosmart.domain.repository

import com.example.agrosmart.domain.models.Fertilizer

interface FertilizerRepository {
    suspend fun getFertilizers(): List<Fertilizer>
    suspend fun fertilizers(): List<Fertilizer> = getFertilizers()
}
