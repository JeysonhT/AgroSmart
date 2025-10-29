
package com.example.agrosmart.data.repository.impl;

import com.example.agrosmart.data.local.dto.DeficiencyDTO;
import com.example.agrosmart.data.local.mappers.DeficiencyMapper;
import com.example.agrosmart.domain.models.Deficiency;
import com.example.agrosmart.domain.repository.DeficiencyRepository;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class DeficiencyRepositoryImpl implements DeficiencyRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    Executor executor = Executors.newSingleThreadExecutor();

    @Override
    public CompletableFuture<List<Deficiency>> getDeficiencies() {
        CompletableFuture<List<Deficiency>> future = new CompletableFuture<>();

        db.collection("Deficiencies")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DeficiencyDTO> deficiencyDTOs = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        DeficiencyDTO deficiencyDTO = document.toObject(DeficiencyDTO.class);
                        deficiencyDTOs.add(deficiencyDTO);
                    }
                    List<Deficiency> deficiencies = deficiencyDTOs.stream()
                            .map(DeficiencyMapper::toModel)
                            .collect(Collectors.toList());

                    future.complete(deficiencies);
                })
                .addOnFailureListener(future::completeExceptionally);
        return future;
    }
}

