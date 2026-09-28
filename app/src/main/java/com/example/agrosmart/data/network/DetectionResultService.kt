package com.example.agrosmart.data.network

import com.example.agrosmart.domain.models.DetectionResult
import com.example.agrosmart.domain.repository.DetectionResultRepository
import java.util.concurrent.CompletableFuture

class DetectionResultService(private val repository: DetectionResultRepository) {
    fun saveDetectionResult(result: DetectionResult?): CompletableFuture<Boolean?>? {
        return repository.saveDetectionResult(result)
    }
}
