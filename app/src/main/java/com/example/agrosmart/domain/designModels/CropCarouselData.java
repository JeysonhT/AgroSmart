package com.example.agrosmart.domain.designModels;

import lombok.Getter;

@Getter
public class CropCarouselData {
    private final int imageResource;
    private final String title;
    private final String description;
    private final String harvestTime;
    private final String type;

    public CropCarouselData(int imageResource, String title, String description, String harvestTime, String type) {
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
        this.harvestTime = harvestTime;
        this.type = type;
    }
}
