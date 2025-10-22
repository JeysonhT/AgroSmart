package com.example.agrosmart.domain.models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DetectionResult {
    private String image64;
    private String result;

    public DetectionResult(String image64, String result) {
        this.image64 = image64;
        this.result = result;
    }

}
