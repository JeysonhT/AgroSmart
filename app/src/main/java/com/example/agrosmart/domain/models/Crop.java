package com.example.agrosmart.domain.models;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter @AllArgsConstructor
public class Crop extends RealmObject {

    @PrimaryKey
    private String _id;
    private String cropName;
    private String description;
    private String content;
    private Long lastUpdate;

    public Crop(){
        this._id = UUID.randomUUID().toString();
    }

    public Crop(String _cropName, String _description){
        this.cropName = _cropName;
        this.description = _description;
    }

    public Crop(String _id, String cropName, String description, String content) {
        this._id = _id;
        this.cropName = cropName;
        this.description = description;
        this.content = content;
    }
}