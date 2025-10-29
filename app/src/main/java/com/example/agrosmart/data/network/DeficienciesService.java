package com.example.agrosmart.data.network;

import com.example.agrosmart.domain.models.Deficiency;
import com.example.agrosmart.domain.repository.DeficiencyRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DeficienciesService {
    private final DeficiencyRepository repository;

    public DeficienciesService(DeficiencyRepository _repository){
        this.repository = _repository;
    }

    public CompletableFuture<List<Deficiency>> getDeficiencies(){return repository.getDeficiencies();}
}
