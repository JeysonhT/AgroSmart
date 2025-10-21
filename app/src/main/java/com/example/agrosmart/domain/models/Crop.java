package com.example.agrosmart.domain.models;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter @AllArgsConstructor
@RealmClass(embedded = true)
public class Crop extends RealmObject {
    private String cropName;
    private String description;
    private String harvestTime;
    private String type;

    public Crop(){
    }

    public Crop(String _cropName, String _harvestTime){
        this.cropName = _cropName;
        this.description = _harvestTime;
    }

    public Crop(String cropName, String description, String harvestTime) {
        this.cropName = cropName;
        this.description = description;
        this.harvestTime = harvestTime;
    }
}