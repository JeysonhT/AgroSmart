package com.example.agrosmart.domain.repository;

import com.example.agrosmart.domain.models.DetectionResult;

import java.util.concurrent.CompletableFuture;

public interface DetectionResultRepository {
    CompletableFuture<Boolean> saveDetectionResult(DetectionResult result);
}
