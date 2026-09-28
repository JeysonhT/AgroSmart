package com.example.agrosmart.domain.repository

import com.example.agrosmart.domain.models.DetectionResult
import java.util.concurrent.CompletableFuture

interface DetectionResultRepository {
    fun saveDetectionResult(result: DetectionResult?): CompletableFuture<Boolean?>?
}
