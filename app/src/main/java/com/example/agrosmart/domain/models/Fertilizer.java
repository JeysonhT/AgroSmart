package com.example.agrosmart.domain.models;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Fertilizer extends RealmObject {
    @PrimaryKey
    private String _id;
    private String dose;
    private String description;
    private String content;
    private Long lastUpdate;

    public Fertilizer(){
        this._id = UUID.randomUUID().toString();
    }
}
