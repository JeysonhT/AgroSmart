package com.example.agrosmart.domain.repository;

import com.example.agrosmart.domain.models.Deficiency;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DeficiencyRepository {
    CompletableFuture<List<Deficiency>> getDeficiencies();
}
