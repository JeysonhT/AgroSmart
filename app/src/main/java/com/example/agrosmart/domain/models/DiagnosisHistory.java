package com.example.agrosmart.domain.models;

import java.util.Date;
import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
@Builder
public class DiagnosisHistory extends RealmObject {
    @PrimaryKey
    private String _id;
    private Crop Crop;
    private Date diagnosisDate;
    private String deficiency;
    private byte[] image;
    private String recommendation;
    private Long lastUpdate;

    public DiagnosisHistory(){
        this._id = UUID.randomUUID().toString();
    }
}
