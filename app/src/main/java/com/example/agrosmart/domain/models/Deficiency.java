package com.example.agrosmart.domain.models;

import java.util.UUID;

public class Deficiency {
    private String _id;
    private byte[] imageResource;
    private String name;
    private String description;
    private String symptoms;
    private String solutions;

    public Deficiency(byte[] _image, String _name, String _description){
        this.imageResource = _image;
        this.name = _name;
        this.description = _description;
    }

    public Deficiency(String _name){
        this.name = _name;
    }

    public Deficiency(){
        this._id = UUID.randomUUID().toString();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public byte[] getImageResource() {
        return imageResource;
    }

    public void setImageResource(byte[] imageResource) {
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getSolutions() {
        return solutions;
    }

    public void setSolutions(String solutions) {
        this.solutions = solutions;
    }
}
