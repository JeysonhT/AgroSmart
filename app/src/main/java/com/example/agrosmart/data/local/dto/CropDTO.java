package com.example.agrosmart.data.local.dto;

import com.example.agrosmart.domain.models.Crop;
import com.google.firebase.firestore.Exclude;

public class CropDTO {
    private String cropName;
    private String description;
    private String content;
    @Exclude
    private String type;

    // 🔑 Firestore necesita un constructor vacío
    public CropDTO() {}

    public CropDTO(String cropName, String description, String content, String type) {
        this.cropName = cropName;
        this.description = description;
        this.content = content;
        this.type = type;
    }

    public CropDTO(String cropName, String description, String content) {
        this.cropName = cropName;
        this.description = description;
        this.content = content;
    }

    public CropDTO(String _name, String _description) {
        this.cropName = _name;
        this.description = _description;
    }

    public String getCropName() { return cropName; }
    public void setCropName(String cropName) { this.cropName = cropName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
