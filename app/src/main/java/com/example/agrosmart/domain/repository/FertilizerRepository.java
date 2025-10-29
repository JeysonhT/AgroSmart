package com.example.agrosmart.domain.repository;

import com.example.agrosmart.domain.models.Fertilizer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FertilizerRepository {
    CompletableFuture<List<Fertilizer>> getFertilizers();
}
