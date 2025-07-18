package com.example.agrosmart.domain.models;

import android.graphics.Bitmap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Deficiency {

    private int deficiency_id;
    private Bitmap image;
    private String name;
    private String description;

    public Deficiency(Bitmap _image, String _name, String _description){
        this.image = _image;
        this.name = _name;
        this.description = _description;
    }

}
