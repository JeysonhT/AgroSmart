package com.example.agrosmart.data.local.dto;


public class MMLResultDTO {
    private String result;
    private Long inferenceTime;
    private Long memoryUse;
    private float[] inferenceData;

    public MMLResultDTO(){}

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getInferenceTime() {
        return inferenceTime;
    }

    public void setInferenceTime(Long inferenceTime) {
        this.inferenceTime = inferenceTime;
    }

    public Long getMemoryUse() {
        return memoryUse;
    }

    public void setMemoryUse(Long memoryUse) {
        this.memoryUse = memoryUse;
    }

    public float[] getInferenceData() {
        return inferenceData;
    }

    public void setInferenceData(float[] inferenceData) {
        this.inferenceData = inferenceData;
    }
}
