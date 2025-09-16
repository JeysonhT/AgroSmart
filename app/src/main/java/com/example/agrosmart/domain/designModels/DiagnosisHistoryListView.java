package com.example.agrosmart.domain.designModels;

import androidx.annotation.NonNull;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DiagnosisHistoryListView {
    private int cropIcon;
    private int deficiencyIcon;
    private String id;
    private String txtDate;
    private String deficiency;
    private byte[] image;
    private String recommendation;

    public DiagnosisHistoryListView() {
    }

    public void setRecommendation(@NonNull String _recommendation){
        if(_recommendation.isBlank()){
            this.recommendation = "Recomendación no generada aun";
        } else {
            this.recommendation = _recommendation;
        }
    }
}
