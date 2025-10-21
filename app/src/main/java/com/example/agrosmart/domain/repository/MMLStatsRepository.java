package com.example.agrosmart.domain.repository;

import com.example.agrosmart.domain.models.MMLStats;

import java.util.concurrent.CompletableFuture;

public interface MMLStatsRepository {
    CompletableFuture<Void> saveInferenceStats(MMLStats mmlStats);
}
