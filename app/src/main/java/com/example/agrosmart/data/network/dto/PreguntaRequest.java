package com.example.agrosmart.data.network.dto;



import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PreguntaRequest {
    @SerializedName("pregunta")
    private String pregunta;

    public PreguntaRequest(String pregunta) {
        this.pregunta = pregunta;
    }
}
