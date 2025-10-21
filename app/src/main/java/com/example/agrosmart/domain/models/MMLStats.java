package com.example.agrosmart.domain.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MMLStats {
    private Long inferenceTime;
    private Long memoryUse;
    private List<Float> inferenceData;

    public void SetInferenceDataFromArray(float[] data){
        this.inferenceData = new ArrayList<>();
        for(float f : data){
            this.inferenceData.add(f);
        }
    }
}
