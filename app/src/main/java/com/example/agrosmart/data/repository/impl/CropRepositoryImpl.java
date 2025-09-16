package com.example.agrosmart.data.repository.impl;

import android.util.Log;

import com.example.agrosmart.core.utils.interfaces.CropsCallback;
import com.example.agrosmart.data.local.dto.CropDTO;
import com.example.agrosmart.domain.models.Crop;
import com.example.agrosmart.domain.models.mappers.CropMapper;
import com.example.agrosmart.domain.repository.CropRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CropRepositoryImpl implements CropRepository {

    private final String TAG = "CROP_REPOSITORY_IMPL";

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void getCrops(CropsCallback callback) {
        List<Crop> crops = new ArrayList<>();

        db.collection("Crops").
                get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            //debug sout
                            //System.out.println(document.get("cropName", String.class).toString());
                            crops.add(new Crop(
                                    document.get("cropName", String.class),
                                    document.get("description", String.class),
                                    document.get("content", String.class)));
                        }
                        callback.onCropsLoaded(crops);
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    @Override
    public void getCropByName(String name, CropsCallback callback) {
        final CropDTO[] crop = new CropDTO[1];

        CollectionReference cropsRF = db.collection("Crops");
        Source source = Source.CACHE;
        Query query = cropsRF.whereEqualTo("cropName", name);

        //obtiene el cultivo de la cache que guarda firebase
        // una vez obtenido los cultivos desde el home fragment
        query.get(source).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);

                crop[0] = new CropDTO(
                        documentSnapshot.get("cropName", String.class),
                        documentSnapshot.get("description", String.class),
                        documentSnapshot.get("content", String.class)
                        );
                Log.println(Log.DEBUG, TAG, "Crop name: " + crop[0].getCropName());
                callback.onCropsLoaded(Collections.singletonList(CropMapper.toEntity(crop[0])));
            } else {
                callback.onError(task.getException());
            }
        });

    }

}
