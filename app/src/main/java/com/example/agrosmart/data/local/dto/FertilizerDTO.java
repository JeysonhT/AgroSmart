package com.example.agrosmart.data.local.dto;

public class FertilizerDTO {
//    private String _id;
    private String name;
    private String applicationMethod;
    private String recommendedDose;
    private String description;
    private String supplier;
    private String type;
//    private Long lastUpdate;

    public FertilizerDTO() {}

//    public String get_id() {
//        return _id;
//    }
//
//    public void set_id(String _id) {
//        this._id = _id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationMethod() {
        return applicationMethod;
    }

    public void setApplicationMethod(String applicationMethod) {
        this.applicationMethod = applicationMethod;
    }

    public String getRecommendedDose() {
        return recommendedDose;
    }

    public void setRecommendedDose(String recommendedDose) {
        this.recommendedDose = recommendedDose;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

//    public Long getLastUpdate() {
//        return lastUpdate;
//    }
//
//    public void setLastUpdate(Long lastUpdate) {
//        this.lastUpdate = lastUpdate;
//    }
}
