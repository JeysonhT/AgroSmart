package com.example.agrosmart.data.local.dto;

public class DeficiencyDTO {
    private String _id;

    private String imageDeficiencies;
    private String title;
    private String description;
    private String symptoms;
    private String solutions;
    //private String imageResource;

    public DeficiencyDTO() {}

//    public String get_id() {
//        return _id;
//    }

//    public void set_id(String _id) {
//        this._id = _id;
//    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getImageDeficiencies() {
        return imageDeficiencies;
    }

    public void setImageDeficiencies(String imageDeficiencies) {
        this.imageDeficiencies = imageDeficiencies;
    }

    //    public String getImageResource() {
//        return imageResource;
//    }
//
//    public void setImageResource(String imageResource) {
//        this.imageResource = imageResource;
//    }
}
