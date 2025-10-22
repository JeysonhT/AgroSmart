package com.example.agrosmart.data.repository.impl;

import android.util.Log;

import com.example.agrosmart.domain.models.DetectionResult;
import com.example.agrosmart.domain.repository.DetectionResultRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

public class DetectionResultRepositoryImpl implements DetectionResultRepository {

    private final String TAG = "DETECTION_RESULT_REPOSITORY";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public CompletableFuture<Boolean> saveDetectionResult(DetectionResult result) {
        CollectionReference c_ref = db.collection("DetectionResult");
        return CompletableFuture.supplyAsync(() -> c_ref.document().
                set(result)
                .addOnCompleteListener((task) -> {
                    if(task.isComplete()){
                        Log.d(TAG, "Deteción subida con exito a firebase");
                    }
                }).addOnFailureListener((ex) -> {
                    Log.e(TAG, "Error al guardar: " + ex);
                }).isComplete());
    }
}
