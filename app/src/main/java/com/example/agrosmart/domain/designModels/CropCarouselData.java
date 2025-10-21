package com.example.agrosmart.domain.designModels;

import lombok.Getter;

@Getter
public class CropCarouselData {
    private final int imageResource;
    private final String title;
    private final String description;

    public CropCarouselData(int imageResource, String title, String description) {
        this.imageResource = imageResource;
        this.title = title;
        this.description = description;
    }
}
