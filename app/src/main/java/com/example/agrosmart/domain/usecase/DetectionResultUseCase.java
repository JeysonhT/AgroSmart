package com.example.agrosmart.domain.usecase;

import com.example.agrosmart.data.network.DetectionResultService;
import com.example.agrosmart.data.repository.impl.DetectionResultRepositoryImpl;
import com.example.agrosmart.domain.models.DetectionResult;

import java.util.concurrent.CompletableFuture;

public class DetectionResultUseCase {
    private final DetectionResultService service;

    public DetectionResultUseCase(){
        this.service = new DetectionResultService(new DetectionResultRepositoryImpl());
    }

    public CompletableFuture<Boolean> saveResult(DetectionResult result){
        return service.saveDetectionResult(result);
    }
}
