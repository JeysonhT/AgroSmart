package com.example.agrosmart.domain.models;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//clase a eliminar
@Getter @Setter @AllArgsConstructor
public class Recommendation extends RealmObject {
    @PrimaryKey
    private String _id;
    private DiagnosisHistory diagnosisHistory;
    private String content;
    private Long lastUpdate;

    public Recommendation(){
        this._id = UUID.randomUUID().toString();
    }
}
