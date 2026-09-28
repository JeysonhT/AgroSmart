package com.example.agrosmart.data.network

import com.example.agrosmart.domain.models.Deficiency
import com.example.agrosmart.domain.repository.DeficiencyRepository
import java.util.concurrent.CompletableFuture

class DeficienciesService(private val repository: DeficiencyRepository) {
    val deficiencies: CompletableFuture<MutableList<Deficiency?>?>?
        get() = repository.deficiencies
}
