package com.example.agrosmart.data.network;

import com.example.agrosmart.domain.models.Fertilizer;
import com.example.agrosmart.domain.repository.FertilizerRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FertilizersService {
    private final FertilizerRepository repository;

    public FertilizersService(FertilizerRepository _repository){
        this.repository = _repository;
    }

    public CompletableFuture<List<Fertilizer>> getFertilizers(){
        return repository.getFertilizers();
    }
}
