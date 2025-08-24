package com.example.agrosmart.data.repository.impl;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.data.network.CropService;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.repository.CropRepository;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CropRepositoryImpl implements CropRepository {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public List<Crop> getCrops(CropsCallback callback) {
        List<Crop> crops = new ArrayList<>();

        db.collection("Crops").
                get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            //debug sout
                            //System.out.println(document.get("cropName", String.class).toString());
                            crops.add(new Crop(document.getId(),
                                    document.get("cropName", String.class),
                                    document.get("description", String.class),
                                    document.get("content", String.class)));
                        }

                        callback.onCropsLoaded(crops);
                    } else {
                        callback.onError(task.getException());
                    }
                });
        return crops;
    }

    @Override
    public void addCropToFavorite(Crop crop) {

    }
}
