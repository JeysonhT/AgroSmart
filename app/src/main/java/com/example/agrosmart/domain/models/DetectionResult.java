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

    public String getImage64() { return image64; }
    public void setImage64(String image64) { this.image64 = image64; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}
