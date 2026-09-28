package com.example.agrosmart.domain.repository

import com.example.agrosmart.domain.models.MMLStats
import java.util.concurrent.CompletableFuture

interface MMLStatsRepository {
    fun saveInferenceStats(mmlStats: MMLStats?): CompletableFuture<Void?>?
}
