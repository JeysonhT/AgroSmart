package com.example.agrosmart.data.repository.impl;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.data.local.dto.CropDTO;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.models.mappers.CropMapper;
import com.example.agrosmart.domain.repository.CropRepository;
import com.facebook.bolts.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CropRepositoryImpl implements CropRepository {

    private final String TAG = "CROP_REPOSITORY_IMPL";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public CompletableFuture<List<Crop>> getCrops() {

        CompletableFuture<List<Crop>> furute = new CompletableFuture<>();

        List<Crop> crops = new ArrayList<>();

        CollectionReference doc_ref = db.collection("Crops");
        doc_ref.get()
                .addOnCompleteListener((task) ->  {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            crops.add(new Crop(
                                    document.get("name", String.class),
                                    document.get("description", String.class),
                                    document.get("harvestTime", String.class),
                                    document.get("type", String.class)
                            ));
                        }
                        furute.complete(crops);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }).addOnFailureListener(furute::completeExceptionally);

        return furute;
    }

    @Override
    public void getCropByName(String name, CropsCallback callback) {
        final CropDTO[] crop = new CropDTO[1];

        CollectionReference cropsRF = db.collection("Crops");
        Source source = Source.CACHE;
        Query query = cropsRF.whereEqualTo("name", name);

        //obtiene el cultivo de la cache que guarda firebase
        // una vez obtenido los cultivos desde el home fragment
        query.get(source).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot document = task.getResult().getDocuments().get(0);

                crop[0] = new CropDTO(
                        document.get("name", String.class),
                        document.get("description", String.class),
                        document.get("harvestTime", String.class),
                        document.get("type", String.class)
                        );
                Log.println(Log.DEBUG, TAG, "Crop name: " + crop[0].getCropName());
                callback.onCropsLoaded(Collections.singletonList(CropMapper.toEntity(crop[0])));
            } else {
                callback.onError(task.getException());
            }
        });

    }

}
