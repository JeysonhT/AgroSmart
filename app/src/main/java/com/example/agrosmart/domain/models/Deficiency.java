package com.example.agrosmart.domain.models;

import android.graphics.Bitmap;

import java.util.UUID;

import io.realm.RealmObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class Deficiency extends RealmObject {
    private String _id;
    private byte[] imageResource;
    private String name;
    private String description;
    private String content;
    private Long lastUpdate;

    public Deficiency(byte[] _image, String _name, String _description){
        this.imageResource = _image;
        this.name = _name;
        this.description = _description;
    }

    public Deficiency(){
        this._id = UUID.randomUUID().toString();
    }
}
