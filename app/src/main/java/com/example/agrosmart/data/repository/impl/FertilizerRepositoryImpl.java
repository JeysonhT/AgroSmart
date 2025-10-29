
package com.example.agrosmart.data.repository.impl;

import com.example.agrosmart.data.local.dto.FertilizerDTO;
import com.example.agrosmart.data.local.mappers.FertilizerMapper;
import com.example.agrosmart.domain.models.Fertilizer;
import com.example.agrosmart.domain.repository.FertilizerRepository;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class FertilizerRepositoryImpl implements FertilizerRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public CompletableFuture<List<Fertilizer>> getFertilizers() {

        CompletableFuture<List<Fertilizer>> future = new CompletableFuture<>();

        db.collection("Fertilizers")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<FertilizerDTO> fertilizerDTOs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        FertilizerDTO fertilizerDTO = document.toObject(FertilizerDTO.class);
                        fertilizerDTOs.add(fertilizerDTO);
                    }
                    List<Fertilizer> fertilizers = fertilizerDTOs.stream()
                            .map(FertilizerMapper::toModel)
                            .collect(Collectors.toList());
                    future.complete(fertilizers);
                })
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}

