package com.example.agrosmart.data.network;

import com.example.agrosmart.domain.models.MMLStats;
import com.example.agrosmart.domain.repository.MMLStatsRepository;

import java.util.concurrent.CompletableFuture;

public class MMLStatsService {
    private MMLStatsRepository repository;

    public MMLStatsService(MMLStatsRepository _repository){
        this.repository = _repository;
    }

    public CompletableFuture<Void> saveStats(MMLStats mmlStats){
        return repository.saveInferenceStats(mmlStats);
    }
}
