package com.example.agrosmart.data.repository.impl;

import android.util.Log;

import com.example.agrosmart.domain.models.MMLStats;
import com.example.agrosmart.domain.repository.MMLStatsRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class MMLStatsRepositoryImpl implements MMLStatsRepository {

    private final String TAG = "MML_REPOSITORY";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public CompletableFuture<Void> saveInferenceStats(MMLStats mmlStats) {
         return CompletableFuture.runAsync(() -> {
            db.collection("MMLStats")
                    .document().set(mmlStats).
                    addOnCompleteListener(onSuccess -> {
                        Log.d(TAG, "Documento guardado exitosamente");
                    }).addOnFailureListener(onFailure -> {
                        Log.e(TAG, "Error al guardar el documento");
                    });
        });
    }

}
