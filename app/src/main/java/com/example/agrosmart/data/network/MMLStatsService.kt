package com.example.agrosmart.data.network

import com.example.agrosmart.domain.models.MMLStats
import com.example.agrosmart.domain.repository.MMLStatsRepository
import java.util.concurrent.CompletableFuture

class MMLStatsService(private val repository: MMLStatsRepository) {
    fun saveStats(mmlStats: MMLStats?): CompletableFuture<Void?>? {
        return repository.saveInferenceStats(mmlStats)
    }
}
