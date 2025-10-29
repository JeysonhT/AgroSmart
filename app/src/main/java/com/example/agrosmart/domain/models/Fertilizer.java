package com.example.agrosmart.domain.models;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Fertilizer extends RealmObject {
    @PrimaryKey
    private String _id;
    private String name;
    private String applicationMethod;
    private String recommendedDose;
    private String description;
    private String supplier;
    private String type;
    private Long lastUpdate;

    public Fertilizer(){
        this._id = UUID.randomUUID().toString();
    }

    public Fertilizer(String _name){
        this.name = _name;
    }

}
