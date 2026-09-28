package com.example.agrosmart.data.network

import com.example.agrosmart.domain.models.Fertilizer
import com.example.agrosmart.domain.repository.FertilizerRepository
import java.util.concurrent.CompletableFuture

class FertilizersService(private val repository: FertilizerRepository) {
    val fertilizers: CompletableFuture<MutableList<Fertilizer?>?>?
        get() = repository.fertilizers
}
