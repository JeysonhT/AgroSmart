package com.example.agrosmart.data.network;

import com.example.agrosmart.domain.models.DetectionResult;
import com.example.agrosmart.domain.repository.DetectionResultRepository;

import java.util.concurrent.CompletableFuture;

public class DetectionResultService {
    private DetectionResultRepository repository;

    public DetectionResultService(DetectionResultRepository _repository){
        this.repository = _repository;
    }

    public CompletableFuture<Boolean> saveDetectionResult(DetectionResult result){
        return repository.saveDetectionResult(result);
    }
}
