package com.example.agrosmart.domain.models;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter @AllArgsConstructor
@RealmClass(embedded = true)
public class Crop extends RealmObject {
    private String cropName;
    private String description;
    private String content;
    private Long lastUpdate;

    public Crop(){
    }

    public Crop(String _cropName, String _description){
        this.cropName = _cropName;
        this.description = _description;
    }

    public Crop(String cropName, String description, String content) {
        this.cropName = cropName;
        this.description = description;
        this.content = content;
    }
}